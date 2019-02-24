package com.provys.provysobject.impl;

import com.provys.provysobject.ProvysNmObject;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface ProvysNmObjectLoader<V extends ProvysNmObjectValue, O extends ProvysNmObject,
        M extends ProvysNmObjectManagerImpl<?, V, O, ?, ?>, P extends ProvysNmObjectProxy<V, O, M>>
        extends ProvysObjectLoader<V, O, M, P> {

    @Nonnull
    Optional<O> loadByNameNm(M manager, String nameNm);
}
