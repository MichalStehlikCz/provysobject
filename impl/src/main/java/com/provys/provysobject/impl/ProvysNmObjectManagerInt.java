package com.provys.provysobject.impl;

import com.provys.provysobject.ProvysNmObject;
import com.provys.provysobject.ProvysNmObjectManager;

@SuppressWarnings("WeakerAccess") // inaccessibility might cause problems in loader subclasses
public interface ProvysNmObjectManagerInt<O extends ProvysNmObject, V extends ProvysNmObjectValue,
        P extends ProvysNmObjectProxy<O, V>> extends ProvysObjectManagerInt<O, V, P>, ProvysNmObjectManager<O> {

}