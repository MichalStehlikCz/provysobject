package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import com.provys.provysobject.ProvysRepository;

public class TestObjectManagerImpl extends
    ProvysObjectManagerImpl<ProvysRepository, TestObject, TestObjectValue,
        TestObjectProxyImpl, TestObjectManagerImpl, TestObjectLoader> {

  TestObjectManagerImpl(ProvysRepository repository, TestObjectLoader loader) {
    super(repository, loader, 10, 0);
  }

  @Override
  protected TestObjectManagerImpl self() {
    return this;
  }

  @Override
  public String getEntityNm() {
    //noinspection SpellCheckingInspection
    return "TESTOBJECT";
  }

  @Override
  protected TestObjectProxyImpl getNewProxy(DtUid id) {
    return new TestObjectProxyImpl(this, id);
  }
}
