package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.util.Objects;

class TestObjectSource {
    @Nonnull
    private final DtUid id;
    @Nonnull
    private final String value;

    TestObjectSource(DtUid id, String value) {
        this.id = Objects.requireNonNull(id);
        this.value = Objects.requireNonNull(value);
    }

    @Nonnull
    DtUid getId() {
        return id;
    }

    @Nonnull
    String getValue() {
        return value;
    }

}
