package com.provys.provysobject.impl;

import com.provys.common.exception.InternalException;

import javax.annotation.Nullable;
import javax.json.bind.annotation.JsonbTransient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Objects;

/**
 * Common ancestor for builders for provys objects with internal name used as unique identifier
 */
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"}) // class used as basis for subclassing in other packages
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ProvysNmObjectValueBuilder<B extends ProvysNmObjectValueBuilder<B, V>,
        V extends ProvysNmObjectValue> extends ProvysObjectValueBuilder<B, V> {

    @Nullable
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
        setNameNm(value.getNameNm());
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
    public B setNameNm(String nameNm) {
        this.nameNm = Objects.requireNonNull(nameNm, "NameNm cannot be null");
        return self();
    }

    /**
     * @return if internal name is set and will be updated
     */
    @JsonbTransient
    public boolean getUpdNameNm() {
        return (nameNm != null);
    }

    /**
     * Set if internal name should be updated. Note that it is not possible to set it to true - you should set value of
     * internal name instead
     *
     * @param updNameNm can be set to false (reset internal name); when set to true, it either does nothing (if original
     *                 value of updNameNm was true) or throws an exception
     * @return self to allow fluent build
     */
    public B setUpdNameNm(boolean updNameNm) {
        if ((nameNm == null) && updNameNm) {
            throw new InternalException("Cannot set updNameNm to true; use setNameNm instead");
        }
        if (!updNameNm) {
            nameNm = null;
        }
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

    @SuppressWarnings("squid:S1206") // id identifies object sufficiently
    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProvysNmObjectValueBuilder<?, ?> that = (ProvysNmObjectValueBuilder<?, ?>) o;
        return Objects.equals(nameNm, that.nameNm);
    }

    @Override
    public String toString() {
        return "ProvysNmObjectValueBuilder{" +
                "nameNm='" + nameNm + '\'' +
                "} " + super.toString();
    }
}