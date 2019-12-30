package com.provys.provysobject.index;

import com.provys.common.datatype.DtUid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Simple value class, holding Id + internal name, used for composite indices
 */
@SuppressWarnings("WeakerAccess") // support class for use in other modules
public final class IdNameNmPair {
    @Nonnull
    private final DtUid id;
    @Nonnull
    private final String nameNm;

    /**
     * Create new id + internal name pair
     *
     * @param id is Id value
     * @param nameNm is internal name value
     */
    public IdNameNmPair(DtUid id, String nameNm) {
        this.id = Objects.requireNonNull(id);
        this.nameNm = Objects.requireNonNull(nameNm);
    }

    /**
     * @return get Id value from pair
     */
    @Nonnull
    public DtUid getId() {
        return id;
    }

    /**
     * @return internal name value from pair
     */
    @Nonnull
    public String getNameNm() {
        return nameNm;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof IdNameNmPair)) return false;
        IdNameNmPair that = (IdNameNmPair) o;
        return getId().equals(that.getId()) &&
                getNameNm().equals(that.getNameNm());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNameNm());
    }

    @Override
    public String toString() {
        return "IdNameNmPair{" +
                "id=" + id +
                ", nameNm='" + nameNm + '\'' +
                '}';
    }
}
