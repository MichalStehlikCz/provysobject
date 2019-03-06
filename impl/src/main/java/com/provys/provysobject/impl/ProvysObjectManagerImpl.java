package com.provys.provysobject.impl;

import com.provys.common.exception.InternalException;
import com.provys.common.exception.RegularException;
import com.provys.provysobject.ProvysObject;
import com.provys.provysobject.ProvysRepository;
import com.provys.provysobject.index.Index;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Gives access to objects of given type, holds objects and ensures there is only one instance for each Id. Keeps
 * indices to find cached objects using other columns than primary key.
 * It uses loader to load object to cache using supplied query (id, name, load all objects...); loader should be
 * supplied on creation and is retained throughout operation.
 * It should receive notification whenever data change and to update its indices; updates are generally received from
 * associated proxy object or loader, manager expects updates or deletions are registered using register and unregister
 * calls but does not make any assumptions about mechanism for receiving these changes.
 * It is up to manager to decide if data should be kept in index or index entries are released and loader is used to
 * retrieve appropriate objects.
 *
 * @param <R> is repository containing given manager. Repository enables crossing entity boundaries - objects within
 *          same repository can reference each other and references are resolved via repository during load time. It
 *           is common for all object managers to keep reference to repository and thus is included here, even though
 *           no other functionality is required of repository on this level
 * @param <V> is value object representing given entity; object manager gets old and new values on object updates to
 *           maintain indices
 * @param <P> proxy type, implementing incremental load for given object interface
 * @param <L> is loader used to verify object existence and load values to objects in repository
 */
public abstract class ProvysObjectManagerImpl<R extends ProvysRepository, O extends ProvysObject,
        V extends ProvysObjectValue, P extends ProvysObjectProxy<O, V>, M extends ProvysObjectManagerInt<O, V, P>,
        L extends ProvysObjectLoader<O, V, P, M>>
        implements ProvysObjectManagerInt<O, V, P> {

    @Nonnull
    private static final Logger LOG = LogManager.getLogger(ProvysObjectManagerImpl.class);

    @Nonnull
    private final R repository;
    @Nonnull
    private final L loader;

    @Nonnull
    private final Map<BigInteger, P> provysObjectById;
    @Nonnull
    private final List<Index<V, P>> indices;

    @SuppressWarnings("WeakerAccess") // class is used for subclassing in other packages
    public ProvysObjectManagerImpl(R repository, L loader, int initialCapacity, int indexCount) {
        this.repository = Objects.requireNonNull(repository);
        this.loader = Objects.requireNonNull(loader);
        this.provysObjectById = new ConcurrentHashMap<>(initialCapacity);
        this.indices = new ArrayList<>(indexCount);
    }

    @Nonnull
    protected abstract M self();

    /**
     * Method used to register index. This index will receive all relevant updates. Method should be called when manager
     * is still empty (e.g. from constructor)
     *
     * @param index is index to be added. Index is directly added to collection, no copying etc.
     * @throws IllegalStateException if called and manager already contains some objects
     */
    @SuppressWarnings("WeakerAccess") // class is used for subclassing in other packages
    protected void addIndex(Index<V, P> index) {
        if (!provysObjectById.isEmpty()) {
            throw new IllegalStateException("Add index method can only be called when manager is empty");
        }
        indices.add(index);
    }

    @SuppressWarnings("WeakerAccess") // method can be used by loader or proxy subclasses
    @Nonnull
    public R getRepository() {
        return repository;
    }

    @Nonnull
    public L getLoader() {
        return loader;
    }

    @Nonnull
    @Override
    public O getById(BigInteger id) {
        return getByIdIfExists(id).
                orElseThrow(() -> new RegularException(LOG, "JAVA_MANAGER_OBJECT_NOT_FOUND",
                        getEntityNm() + " not found by id: " + id,
                        Map.of("ENTITY_NM", getEntityNm(), "ID", id.toString())));
    }

    @Nonnull
    @Override
    public Optional<O> getByIdIfExists(BigInteger id) {
        P provysObject = provysObjectById.get(Objects.requireNonNull(id));
        if (provysObject != null) {
            // object found in cache
            return Optional.of(provysObject.selfAsObject());
        }
        // object has to be loaded
        return getLoader().loadById(self(), id).map(ProvysObjectProxy::selfAsObject);
    }

    @Nonnull
    @Override
    public Collection<O> getAll() {
        getLoader().loadAll(self());
        return provysObjectById.values().stream().map(ProvysObjectProxy::selfAsObject).collect(Collectors.toList());
    }

    @Nonnull
    protected abstract P getNewProxy(BigInteger id);

    /**
     * Retrieve entity group if already loaded to cache, otherwise create new proxy for given id. Should only be called
     * internally as method does not verify existence of given object in database.
     *
     * @param id is Id of entity group being looked for
     * @return entity group present in cache or newly added proxy
     */
    @Nonnull
    @Override
    public P getOrAddById(BigInteger id) {
        return provysObjectById.computeIfAbsent(Objects.requireNonNull(id), this::getNewProxy);
    }

    /**
     * Use loader to load value to proxy
     */
    @Override
    public void loadValueObject(P objectProxy) {
        getLoader().loadValue(self(), objectProxy);
    }

    /**
     * Register given object in indices. Verifies that object proxy has been previously registered for its id, if not,
     * throws exception, otherwise defers change registration to doRegisterChange method
     */
    @Override
    public void registerUpdate(P objectProxy, @Nullable V oldValue, @Nullable V newValue) {
        if (provysObjectById.get(objectProxy.getId()) == objectProxy) {
            for (var index : indices) {
                index.update(objectProxy, oldValue, newValue);
            }
        } else {
            throw new InternalException(LOG,
                    "Register change " + getEntityNm() + " called on unregistered object proxy");
        }
    }

    /**
     * Remove given object. Used as reaction to delete. Note that even if references are removed from indices,
     * there might still be objects that retain reference and thus might stumble across invalid object proxy
     */
    @Override
    public void registerDelete(P objectProxy, @Nullable V oldValue) {
        // remove from primary index
        if (provysObjectById.remove(objectProxy.getId(), objectProxy)) {
            // if it was registered, remove from all other indices
            if (oldValue != null) {
                for (var index : indices) {
                    index.delete(objectProxy, oldValue);
                }
            }
        } else {
            // just log warning, no point trying to remove value
            LOG.warn("Call to delete from {} manager invoked on not registered object {}", getEntityNm(),
                    objectProxy);
        }
    }

    @Override
    public void unregister(P objectProxy, @Nullable V oldValue) {
        // remove from primary index
        if (provysObjectById.remove(objectProxy.getId(), objectProxy)) {
            // if it was registered, remove from all other indices
            if (oldValue != null) {
                for (var index : indices) {
                    // we register it as update from value  to null, as it invalidates sets object was contained in
                    index.update(objectProxy, oldValue, null);
                }
            }
        } else {
            // just log warning, no point trying to remove value
            LOG.warn("Call to delete from {} manager invoked on not registered object {}", getEntityNm(),
                    objectProxy);
        }
    }

    @Override
    public void registerUnknownUpdate() {
        for (var index : indices) {
            index.unknownUpdate();
        }
    }
}
