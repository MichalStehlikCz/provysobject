package com.provys.provysobject.impl;

import com.provys.provysobject.ProvysNmObject;

import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigInteger;

@SuppressWarnings("WeakerAccess") // used as basis for subclassing in other modules
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ProvysNmObjectProxyImpl<O extends ProvysNmObject, V extends ProvysNmObjectValue,
        P extends ProvysNmObjectProxy<O, V>, M extends ProvysNmObjectManagerInt<O, V, P>>
        extends ProvysObjectProxyImpl<O, V, P, M> implements ProvysNmObjectProxy<O, V>,  ProvysNmObject {

    @SuppressWarnings("WeakerAccess") // class used as basis for subclassing in other packages
    protected ProvysNmObjectProxyImpl(M manager, BigInteger id) {
        super(manager, id);
    }

    @XmlElement(name = "NAME_NM")
    @Nonnull
    @Override
    public String getNameNm() {
        return validateValueObject().getNameNm();
    }

}
