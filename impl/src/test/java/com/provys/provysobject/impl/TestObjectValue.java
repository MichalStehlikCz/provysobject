package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Value class used for test of ProvysObject related classes
 */
class TestObjectValue extends ProvysObjectValue {

    @Nonnull
    private final String value;

    TestObjectValue(DtUid id, String value) {
        super(id);
        this.value = Objects.requireNonNull(value);
    }

    @Nonnull
    String getValue() {
        return value;
    }

    @Override
    @SuppressWarnings("squid:S1206") // hash in parent uses id which is sufficient
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestObjectValue)) return false;
        if (!super.equals(o)) return false;
        TestObjectValue that = (TestObjectValue) o;
        return Objects.equals(getValue(), that.getValue());
    }
}
