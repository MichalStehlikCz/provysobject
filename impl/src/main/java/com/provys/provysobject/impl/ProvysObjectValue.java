package com.provys.provysobject.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Immutable value class holding values of properties of given PROVYS object.
 * Created by corresponding loader and used internally by given proxy.
 */
@SuppressWarnings("WeakerAccess") // basis for subclassing in other packages
public abstract class ProvysObjectValue {

    @Nonnull
    private final BigInteger id;

    public ProvysObjectValue(BigInteger id) {
        this.id = Objects.requireNonNull(id);
    }

    @Nonnull
    public BigInteger getId() {
        return id;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof ProvysObjectValue)) return false;
        ProvysObjectValue that = (ProvysObjectValue) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
