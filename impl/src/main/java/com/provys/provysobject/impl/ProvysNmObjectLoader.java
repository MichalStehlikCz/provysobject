package com.provys.provysobject.impl;

import com.provys.provysobject.ProvysNmObject;

import javax.annotation.Nonnull;
import java.util.Optional;

@SuppressWarnings("WeakerAccess") // extended when implementing support for particular entities in other packages
public interface ProvysNmObjectLoader<O extends ProvysNmObject, V extends ProvysNmObjectValue,
        P extends ProvysNmObjectProxy<O, V>, M extends ProvysNmObjectManagerInt<O, V, P>>
        extends ProvysObjectLoader<O, V, P, M> {

    @Nonnull
    Optional<O> loadByNameNm(M manager, String nameNm);
}
