package com.provys.provysobject.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.provys.common.datatype.DtUid;
import org.checkerframework.checker.nullness.qual.Nullable;

@SuppressWarnings({"EqualsAndHashcode", "squid:S1206"})
// using Id as hash code is sufficient, no need to add additional fields in there
public abstract class ProvysNmObjectValue extends ProvysObjectValue {

  private final String nameNm;

  public ProvysNmObjectValue(DtUid id, String nameNm) {
    super(id);
    this.nameNm = nameNm;
  }

  @JsonProperty("NAME_NM")
  public String getNameNm() {
    return nameNm;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ProvysNmObjectValue)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    ProvysNmObjectValue that = (ProvysNmObjectValue) o;
    return nameNm.equals(that.nameNm);
  }

  @Override
  public String toString() {
    return "ProvysNmObjectValue{"
        + "nameNm='" + nameNm + '\''
        + ", " + super.toString() + '}';
  }
}
