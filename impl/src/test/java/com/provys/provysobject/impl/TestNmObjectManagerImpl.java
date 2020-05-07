package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import com.provys.provysobject.ProvysRepository;

class TestNmObjectManagerImpl extends ProvysNmObjectManagerImpl<ProvysRepository, TestNmObject,
    TestNmObjectValue, TestNmObjectProxyImpl, TestNmObjectManagerImpl, TestNmObjectLoader> {

  TestNmObjectManagerImpl(ProvysRepository repository, TestNmObjectLoader loader) {
    super(repository, loader, 10, 0);
  }

  @Override
  protected TestNmObjectManagerImpl self() {
    return this;
  }

  @Override
  public String getEntityNm() {
    //noinspection SpellCheckingInspection
    return "TESTNMOBJECT";
  }

  @Override
  protected TestNmObjectProxyImpl getNewProxy(DtUid id) {
    return new TestNmObjectProxyImpl(this, id);
  }
}
