package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import java.util.Objects;

/**
 * Test class used for by {@code TestNmObjectLoaderImpl} for testing of
 * {@code ProvysNmObjectLoaderImpl}.
 */
class TestNmObjectSource {

    private final DtUid id;
    private final String nameNm;
    private final String value;

    TestNmObjectSource(DtUid id, String nameNm, String value) {
        this.id = Objects.requireNonNull(id);
        this.nameNm = Objects.requireNonNull(nameNm);
        this.value = Objects.requireNonNull(value);
    }

    DtUid getId() {
        return id;
    }

    String getNameNm() {
        return nameNm;
    }

    String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TestNmObjectSource{"
            + "id=" + id
            + ", nameNm='" + nameNm + '\''
            + ", value='" + value + '\''
            + '}';
    }
}
