package com.provys.provysobject.impl;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.util.Optional;

public class TestNmObjectProxyImpl extends ProvysNmObjectProxyImpl<TestNmObject, TestNmObjectValue,
        TestNmObjectProxyImpl, TestNmObjectManagerImpl> implements TestNmObject {

    TestNmObjectProxyImpl(TestNmObjectManagerImpl manager, BigInteger id) {
        super(manager, id);
    }

    @Nonnull
    @Override
    protected TestNmObjectProxyImpl self() {
        return this;
    }

    @Nonnull
    @Override
    public TestNmObject selfAsObject() {
        return this;
    }

    @Override
    public Optional<String> getValue() {
        return validateValueObject().getValue();
    }
}
