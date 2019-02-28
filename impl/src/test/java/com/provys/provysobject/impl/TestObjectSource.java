package com.provys.provysobject.impl;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.util.Objects;

class TestObjectSource {
    @Nonnull
    private final BigInteger id;
    @Nonnull
    private final String value;

    TestObjectSource(BigInteger id, String value) {
        this.id = Objects.requireNonNull(id);
        this.value = Objects.requireNonNull(value);
    }

    @Nonnull
    BigInteger getId() {
        return id;
    }

    @Nonnull
    String getValue() {
        return value;
    }

}
