package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlTransient;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Immutable value class holding values of properties of given PROVYS object.
 * Created by corresponding loader and used internally by given proxy.
 */
@SuppressWarnings("WeakerAccess") // basis for subclassing in other packages
@XmlTransient
public abstract class ProvysObjectValue {

    @Nonnull
    private final DtUid id;

    public ProvysObjectValue(DtUid id) {
        this.id = Objects.requireNonNull(id);
    }

    @Nonnull
    public DtUid getId() {
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
