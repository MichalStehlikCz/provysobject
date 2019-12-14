package com.provys.provysobject.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigInteger;
import java.util.Objects;

@SuppressWarnings("WeakerAccess") // class used as basis for subclassing in other packages
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ProvysNmObjectValue extends ProvysObjectValue {

    @Nonnull
    private final String nameNm;

    public ProvysNmObjectValue(BigInteger id, String nameNm) {
        super(id);
        this.nameNm = Objects.requireNonNull(nameNm);
    }

    @XmlElement(name = "NAME_NM")
    @Nonnull
    public String getNameNm() {
        return nameNm;
    }

    @Override
    @SuppressWarnings("squid:S1206") // using Id as hash code is sufficient, no need to add additional fields
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof ProvysNmObjectValue)) return false;
        if (!super.equals(o)) return false;
        ProvysNmObjectValue that = (ProvysNmObjectValue) o;
        return Objects.equals(getNameNm(), that.getNameNm());
    }
}
