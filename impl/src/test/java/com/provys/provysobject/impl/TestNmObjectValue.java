package com.provys.provysobject.impl;

import java.math.BigInteger;
import java.util.Objects;

class TestNmObjectValue extends ProvysNmObjectValue {

    private final String value;

    TestNmObjectValue(BigInteger id, String nameNm, String value) {
        super(id, nameNm);
        this.value = Objects.requireNonNull(value);
    }

    String getValue() {
        return value;
    }

    @Override
    @SuppressWarnings("squid:S1206") // hash in parent uses id which is sufficient
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestNmObjectValue)) return false;
        if (!super.equals(o)) return false;
        TestNmObjectValue that = (TestNmObjectValue) o;
        return Objects.equals(getValue(), that.getValue());
    }
}
