package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import com.provys.provysobject.ProvysObject;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.util.Optional;

/**
 * Enables load of value to proxy for given entity and registration proxies based on supplied condition - usually Id,
 * name or all objects of given type
 *
 * @param <V> is value type for given entity; loader acts as factory for these value objects
 * @param <P> is proxy class for given entity
 */
@SuppressWarnings("WeakerAccess") // extended when implementing support for particular entities in other packages
public interface ProvysObjectLoader<O extends ProvysObject, V extends ProvysObjectValue,
        P extends ProvysObjectProxy<O, V>, M extends ProvysObjectManagerInt<O, V, P>> {
    @Nonnull
    Optional<P> loadById(M manager, DtUid id);

    void loadValue(M manager, P entityGrpProxy);

    void loadAll(M manager);
}
