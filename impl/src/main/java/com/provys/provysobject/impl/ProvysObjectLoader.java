package com.provys.provysobject.impl;

import com.provys.provysobject.ProvysObject;

import javax.annotation.Nonnull;
import java.math.BigInteger;

/**
 * Enables load of value to proxy for given entity and registration proxies based on supplied condition - usually Id,
 * name or all objects of given type
 *
 * @param <V> is value type for given entity; loader acts as factory for these value objects
 * @param <O> is interface corresponding to given entity
 * @param <M> is entity manager, tracking and managing instances of given entity
 * @param <P> is proxy class used for given entity
 */
public interface ProvysObjectLoader<V extends ProvysObjectValue, O extends ProvysObject,
        M extends ProvysObjectManagerImpl<?, V, O, ?, ?>, P extends ProvysObjectProxy<V, O, M>> {
    @Nonnull
    O loadById(M manager, BigInteger id);

    void loadValue(P entityGrpProxy);

    void loadAll(M manager);
}
