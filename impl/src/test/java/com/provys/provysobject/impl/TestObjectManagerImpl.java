package com.provys.provysobject.impl;

import com.provys.provysobject.ProvysRepository;

import javax.annotation.Nonnull;
import java.math.BigInteger;

class TestObjectManagerImpl extends ProvysObjectManagerImpl<ProvysRepository, TestObject, TestObjectValue,
        TestObjectProxyImpl, TestObjectManagerImpl, TestObjectLoader> {

    TestObjectManagerImpl(ProvysRepository repository, TestObjectLoader loader) {
        super(repository, loader, 10, 0);
    }

    @Nonnull
    @Override
    protected TestObjectManagerImpl self() {
        return this;
    }

    @Nonnull
    @Override
    public String getEntityNm() {
        //noinspection SpellCheckingInspection
        return "TESTOBJECT";
    }

    @Nonnull
    @Override
    protected TestObjectProxyImpl getNewProxy(BigInteger id) {
        return new TestObjectProxyImpl(this, id);
    }

}
