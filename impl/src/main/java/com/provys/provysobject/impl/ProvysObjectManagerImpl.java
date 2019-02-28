package com.provys.provysobject.impl;

import com.provys.common.exception.InternalException;
import com.provys.common.exception.RegularException;
import com.provys.provysobject.ProvysObject;
import com.provys.provysobject.ProvysRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

    ProvysObjectManagerImpl(R repository, L loader, int initialCapacity) {
        this.repository = Objects.requireNonNull(repository);
        this.loader = Objects.requireNonNull(loader);
        this.provysObjectById = new ConcurrentHashMap<>(initialCapacity);
    }

    @Nonnull
    protected abstract M self();

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
     * Does actual updating of indices.
     * Called from registerChange in case proxy is found to be registered in this manager.
     *
     * @param provysObject is proxy to object to be registered
     * @param oldValue are old values associated with object
     * @param newValue are new values associated with object
     */
    @SuppressWarnings("WeakerAccess") // method needs to be overloaded in subclasses
    protected void doRegisterChange(P provysObject, @Nullable V oldValue, @Nullable V newValue) {}

    /**
     * Register given object in indices. Verifies that object proxy has been previously registered for its id, if not,
     * throws exception
     *
     * @param provysObject is proxy to object to be registered
     * @param oldValue are old values associated with object
     * @param newValue are new values associated with object
     */
    @Override
    public void registerChange(P provysObject, @Nullable V oldValue, @Nullable V newValue) {
        if (provysObjectById.get(provysObject.getId()) == provysObject) {
            doRegisterChange(provysObject, oldValue, newValue);
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
    public void unregister(P provysObject, @Nullable V oldValue) {
        // remove from additional indices
        if (oldValue != null) {
            registerChange(provysObject, oldValue, null);
        }
        // remove from primary index
        var oldProvysObject = provysObjectById.remove(provysObject.getId());
        if ((oldProvysObject != null) && (oldProvysObject != provysObject)) {
            // if different object has been registered here, return it back
            provysObjectById.putIfAbsent(oldProvysObject.getId(), oldProvysObject);
        }
    }
}
