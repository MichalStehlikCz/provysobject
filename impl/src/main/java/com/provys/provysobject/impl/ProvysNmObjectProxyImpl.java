package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import com.provys.provysobject.ProvysNmObject;

/**
 * Proxy ancestor for objects identified by internal name as natural key.
 *
 * @param <O> is interface representing object
 * @param <V> is corresponding value type
 * @param <P> is proxy used to access value
 * @param <M> is manager used as repository for this proxy
 */
public abstract class ProvysNmObjectProxyImpl<O extends ProvysNmObject,
    V extends ProvysNmObjectValue, P extends ProvysNmObjectProxy<O, V>,
    M extends ProvysNmObjectManagerInt<O, V, P>>
    extends ProvysObjectProxyImpl<O, V, P, M> implements ProvysNmObjectProxy<O, V> {

  protected ProvysNmObjectProxyImpl(M manager, DtUid id) {
    super(manager, id);
  }

  @Override
  public String getNameNm() {
    return validateValueObject().getNameNm();
  }
}
