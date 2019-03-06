package com.provys.provysobject.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

public class TestNmObjectValue extends ProvysNmObjectValue {

    @Nullable
    private final String value;

    public TestNmObjectValue(BigInteger id, String nameNm, @Nullable String value) {
        super(id, nameNm);
        this.value = value;
    }

    @Nonnull
    public Optional<String> getValue() {
        return Optional.ofNullable(value);
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
