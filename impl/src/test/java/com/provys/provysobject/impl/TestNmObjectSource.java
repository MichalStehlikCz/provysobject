package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Test class used for by {@code TestNmObjectLoaderImpl} for testing of {@code ProvysNmObjectLoaderImpl}
 */
class TestNmObjectSource {
    @Nonnull
    private final DtUid id;
    @Nonnull
    private final String nameNm;
    @Nonnull
    private final String value;

    TestNmObjectSource(DtUid id, String nameNm, String value) {
        this.id = Objects.requireNonNull(id);
        this.nameNm = Objects.requireNonNull(nameNm);
        this.value = Objects.requireNonNull(value);
    }

    @Nonnull
    DtUid getId() {
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
