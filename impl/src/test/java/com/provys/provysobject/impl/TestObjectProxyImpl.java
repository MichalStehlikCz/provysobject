package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;

class TestObjectProxyImpl extends
    ProvysObjectProxyImpl<TestObject, TestObjectValue, TestObjectProxyImpl, TestObjectManagerImpl>
    implements TestObject {

  TestObjectProxyImpl(TestObjectManagerImpl manager, DtUid id) {
    super(manager, id);
  }

  @Override
  public TestObject selfAsObject() {
    return this;
  }

  @Override
  protected TestObjectProxyImpl self() {
    return this;
  }

  @Override
  public String getValue() {
    return validateValueObject().getValue();
  }
}
