package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import com.provys.provysobject.ProvysNmObject;

import javax.annotation.Nonnull;

@SuppressWarnings("WeakerAccess") // used as basis for subclassing in other modules
public abstract class ProvysNmObjectProxyImpl<O extends ProvysNmObject, V extends ProvysNmObjectValue,
        P extends ProvysNmObjectProxy<O, V>, M extends ProvysNmObjectManagerInt<O, V, P>>
        extends ProvysObjectProxyImpl<O, V, P, M> implements ProvysNmObjectProxy<O, V>,  ProvysNmObject {

    @SuppressWarnings("WeakerAccess") // class used as basis for subclassing in other packages
    protected ProvysNmObjectProxyImpl(M manager, DtUid id) {
        super(manager, id);
    }

    @Nonnull
    @Override
    public String getNameNm() {
        return validateValueObject().getNameNm();
    }

}
