package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import com.provys.provysobject.ProvysRepository;

import javax.annotation.Nonnull;
import java.math.BigInteger;

class TestNmObjectManagerImpl extends ProvysNmObjectManagerImpl<ProvysRepository, TestNmObject,
        TestNmObjectValue, TestNmObjectProxyImpl, TestNmObjectManagerImpl, TestNmObjectLoader> {

    TestNmObjectManagerImpl(ProvysRepository repository, TestNmObjectLoader loader) {
        super(repository, loader, 10, 0);
    }

    @Nonnull
    @Override
    protected TestNmObjectManagerImpl self() {
        return this;
    }

    @Nonnull
    @Override
    public String getEntityNm() {
        //noinspection SpellCheckingInspection
        return "TESTNMOBJECT";
    }

    @Nonnull
    @Override
    protected TestNmObjectProxyImpl getNewProxy(DtUid id) {
        return new TestNmObjectProxyImpl(this, id);
    }

}
