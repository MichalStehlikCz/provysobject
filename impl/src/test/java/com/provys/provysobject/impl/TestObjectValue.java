package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Value class used for test of ProvysObject related classes
 */
// hash in parent uses id which is sufficient
@SuppressWarnings({"EqualsAndHashcode", "squid:S1206"})
class TestObjectValue extends ProvysObjectValue {

  private final String value;

  TestObjectValue(DtUid id, String value) {
    super(id);
    this.value = Objects.requireNonNull(value);
  }

  String getValue() {
    return value;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TestObjectValue)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    TestObjectValue that = (TestObjectValue) o;
    return getValue().equals(that.getValue());
  }

  @Override
  public String toString() {
    return "TestObjectValue{"
        + "value='" + value + '\''
        + ", " + super.toString() + '}';
  }
}
