package com.provys.provysobject.impl;

import com.provys.common.exception.InternalException;
import com.provys.provysobject.ProvysObject;
import com.provys.provysobject.ProvysObjectManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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
 * @param <R>
 * @param <V>
 * @param <T>
 * @param <L>
 */
abstract public class ProvysObjectManagerImpl<R extends ProvysRepositoryImpl, V extends ProvysObjectValue,
        O extends ProvysObject, M extends ProvysObjectManagerImpl<R, V, O, ?, ?>,
        L extends ProvysObjectLoader<V, O, M, ? extends ProvysObjectProxy<V, O, M>>>
        implements ProvysObjectManager<O> {

    @Nonnull
    private static final Logger LOG = LogManager.getLogger(ProvysObjectManagerImpl.class);

    @Nonnull
    private final R repository;
    @Nonnull
    private final L loader;

    @Nonnull
    private final Map<BigInteger, O> provysObjectById;

    ProvysObjectManagerImpl(R repository, L loader, int initialCapacity) {
        this.repository = Objects.requireNonNull(repository);
        this.loader = Objects.requireNonNull(loader);
        this.provysObjectById = new ConcurrentHashMap<>(initialCapacity);
    }

    @Nonnull
    abstract protected M self();

    @Nonnull
    public R getRepository() {
        return repository;
    }

    @Nonnull
    public L getLoader() {
        return loader;
    }

    /**
     * @return internal name of entity this manager belongs to. Used in error messages
     */
    @Nonnull
    abstract public String getEntityNm();

    @Nonnull
    @Override
    public O getById(BigInteger id) {
        O provysObject = provysObjectById.get(Objects.requireNonNull(id));
        if (provysObject == null) {
            provysObject = getLoader().loadById(self(), id);
        }
        return provysObject;
    }

    @Nonnull
    @Override
    public Collection<O> getAll() {
        getLoader().loadAll(self());
        return Collections.unmodifiableCollection(provysObjectById.values());
    }

    abstract protected O getNewProxy(BigInteger id);

    /**
     * Retrieve entity group if already loaded to cache, otherwise create new proxy for given id. Should only be called
     * internally as method does not verify existence of given object in database.
     *
     * @param id is Id of entity group being looked for
     * @return entity group present in cache or newly added proxy
     */
    public O getOrAddById(BigInteger id) {
        return provysObjectById.computeIfAbsent(Objects.requireNonNull(id), this::getNewProxy);
    }

    /**
     * Register given object in indices. Verifies that object proxy has been previously registered for its id, if not,
     * throws exception
     *
     * @param provysObject is proxy to object to be registered
     * @param oldValue are old values associated with object
     * @param newValue are new values associated with object
     */
    void registerChange(O provysObject, @Nullable V oldValue, @Nullable V newValue) {
        if (provysObjectById.get(provysObject.getId()) != provysObject) {
            throw new InternalException(LOG, "Register change called on unregistered object proxy");
        }
    }

    /**
     * Remove given object. Used as reaction to delete. Note that even if references are removed from indices,
     * there might still be objects that retain reference and thus might stumble across invalid object proxy
     */
    void unregister(O provysObject, @Nullable V oldValue) {
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
