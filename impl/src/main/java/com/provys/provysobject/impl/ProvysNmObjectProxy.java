package com.provys.provysobject.impl;

import com.provys.provysobject.ProvysNmObject;

import javax.annotation.Nonnull;
import java.math.BigInteger;

abstract public class ProvysNmObjectProxy<V extends ProvysNmObjectValue, O extends ProvysNmObject,
        M extends ProvysNmObjectManagerImpl<?, V, O, ?, ?>>
        extends ProvysObjectProxy<V, O, M> implements ProvysNmObject {

    public ProvysNmObjectProxy(M manager, BigInteger id) {
        super(manager, id);
    }

    @Nonnull
    @Override
    public String getNameNm() {
        return getValue().getNameNm();
    }

}
