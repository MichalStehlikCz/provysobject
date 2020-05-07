package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Immutable value class holding values of properties of given PROVYS object. Created by
 * corresponding loader and used internally by given proxy.
 */
public abstract class ProvysObjectValue {

  private final DtUid id;

  public ProvysObjectValue(DtUid id) {
    this.id = id;
  }

  public DtUid getId() {
    return id;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ProvysObjectValue)) {
      return false;
    }
    ProvysObjectValue that = (ProvysObjectValue) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "ProvysObjectValue{"
        + "id=" + id
        + '}';
  }
}
