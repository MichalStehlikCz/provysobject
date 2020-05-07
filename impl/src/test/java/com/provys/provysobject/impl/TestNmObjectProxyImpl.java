package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TestNmObjectProxyImpl extends ProvysNmObjectProxyImpl<TestNmObject, TestNmObjectValue,
        TestNmObjectProxyImpl, TestNmObjectManagerImpl> implements TestNmObject {

    TestNmObjectProxyImpl(TestNmObjectManagerImpl manager, DtUid id) {
        super(manager, id);
    }

    @Override
    protected TestNmObjectProxyImpl self() {
        return this;
    }

    @Override
    public TestNmObject selfAsObject() {
        return this;
    }

    @Override
    public @Nullable String getValue() {
        return validateValueObject().getValue();
    }
}
