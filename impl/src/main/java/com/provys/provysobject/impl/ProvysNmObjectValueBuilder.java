package com.provys.provysobject.impl;

import javax.annotation.Nullable;
import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.Objects;

/**
 * Common ancestor for builders for provys objects with internal name used as unique identifier
 */
@SuppressWarnings("WeakerAccess") // class used as basis for subclassing in other packages
public abstract class ProvysNmObjectValueBuilder<B extends ProvysNmObjectValueBuilder<B, V>,
        V extends ProvysNmObjectValue> extends ProvysObjectValueBuilder<B, V> {

    @Nullable
    @NotNull
    private String nameNm;

    /**
     * Create empty builder
     */
    public ProvysNmObjectValueBuilder() {
    }

    /**
     * Create builder based on supplied value object
     *
     * @param value is value objects values should be copied from
     */
    public ProvysNmObjectValueBuilder(V value) {
        super(value);
    }

    /**
     * Create identical copy of builder
     *
     * @param value is source builder copy should be created from
     */
    public ProvysNmObjectValueBuilder(B value) {
        super(value);
        this.nameNm = value.getNameNm();
    }

    /**
     * @return value of field nameNm
     */
    @JsonbProperty("NAME_NM")
    @XmlElement(name = "NAME_NM")
    @Nullable
    public String getNameNm() {
        return nameNm;
    }

    /**
     * Set value of field nameNm
     *
     * @param nameNm is new value to be set
     * @return self to enable chaining
     */
    public ProvysNmObjectValueBuilder<B, V> setNameNm(String nameNm) {
        this.nameNm = Objects.requireNonNull(nameNm, "NameNm cannot be null");
        return self();
    }

    @Override
    public B apply(V source) {
        if (nameNm == null) {
            this.nameNm = source.getNameNm();
        }
        return super.apply(source);
    }

    @Override
    public boolean notChanged(V source) {
        if ((nameNm != null) && (!nameNm.equals(source.getNameNm()))) {
            return false;
        }
        return super.notChanged(source);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProvysNmObjectValueBuilder<?, ?> that = (ProvysNmObjectValueBuilder<?, ?>) o;
        return Objects.equals(nameNm, that.nameNm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nameNm);
    }

    @Override
    public String toString() {
        return "ProvysNmObjectValueBuilder{" +
                "nameNm='" + nameNm + '\'' +
                "} " + super.toString();
    }
}