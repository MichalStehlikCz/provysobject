package com.provys.provysobject.impl;

import javax.annotation.Nonnull;
import java.math.BigInteger;

class TestObjectProxyImpl extends ProvysObjectProxyImpl<TestObject, TestObjectValue, TestObjectProxyImpl,
        TestObjectManagerImpl> implements TestObject {

    TestObjectProxyImpl(TestObjectManagerImpl manager, BigInteger id) {
        super(manager, id);
    }

    @Nonnull
    @Override
    public TestObject selfAsObject() {
        return this;
    }

    @Nonnull
    @Override
    protected TestObjectProxyImpl self() {
        return this;
    }

    @Override
    public String getValue() {
        return validateValueObject().getValue();
    }

}
