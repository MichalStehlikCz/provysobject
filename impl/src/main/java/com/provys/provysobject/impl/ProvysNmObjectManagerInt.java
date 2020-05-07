package com.provys.provysobject.impl;

import com.provys.provysobject.ProvysNmObject;
import com.provys.provysobject.ProvysNmObjectManager;

/**
 * Interface representing object manager; need to be public, as otherwise it causes problems in
 * loader packages.
 *
 * @param <O> is object this manager acts as repository for
 * @param <V> is corresponding value object
 * @param <P> is proxy implementing access to value object
 */
public interface ProvysNmObjectManagerInt<O extends ProvysNmObject, V extends ProvysNmObjectValue,
    P extends ProvysNmObjectProxy<O, V>> extends ProvysObjectManagerInt<O, V, P>,
    ProvysNmObjectManager<O> {

}
