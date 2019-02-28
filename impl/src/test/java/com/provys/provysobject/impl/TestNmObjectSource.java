package com.provys.provysobject.impl;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Test class used for by {@code TestNmObjectLoaderImpl} for testing of {@code ProvysNmObjectLoaderImpl}
 */
class TestNmObjectSource {
    @Nonnull
    private final BigInteger id;
    @Nonnull
    private final String nameNm;
    @Nonnull
    private final String value;

    TestNmObjectSource(BigInteger id, String nameNm, String value) {
        this.id = Objects.requireNonNull(id);
        this.nameNm = Objects.requireNonNull(nameNm);
        this.value = Objects.requireNonNull(value);
    }

    @Nonnull
    BigInteger getId() {
        return id;
    }

    @Nonnull
    String getNameNm() {
        return nameNm;
    }

    @Nonnull
    String getValue() {
        return value;
    }

}
