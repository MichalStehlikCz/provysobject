package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import java.util.Objects;

class TestObjectSource {

  private final DtUid id;
  private final String value;

  TestObjectSource(DtUid id, String value) {
    this.id = Objects.requireNonNull(id);
    this.value = Objects.requireNonNull(value);
  }

  DtUid getId() {
    return id;
  }

  String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "TestObjectSource{"
        + "id=" + id
        + ", value='" + value + '\''
        + '}';
  }
}
