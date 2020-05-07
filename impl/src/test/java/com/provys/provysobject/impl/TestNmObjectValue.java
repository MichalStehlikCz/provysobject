package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import java.util.Objects;
import java.util.Optional;
import org.checkerframework.checker.nullness.qual.Nullable;

@SuppressWarnings("EqualsAndHashcode") // hash in parent uses id which is sufficient
public class TestNmObjectValue extends ProvysNmObjectValue {

  private final @Nullable String value;

  public TestNmObjectValue(DtUid id, String nameNm, @Nullable String value) {
    super(id, nameNm);
    this.value = value;
  }

  public @Nullable String getValue() {
    return value;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TestNmObjectValue)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    TestNmObjectValue that = (TestNmObjectValue) o;
    return Objects.equals(getValue(), that.getValue());
  }

  @Override
  public String toString() {
    return "TestNmObjectValue{"
        + "value='" + value + '\''
        + ", " + super.toString() + '}';
  }
}
