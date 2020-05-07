package com.provys.provysobject.impl;

import static org.assertj.core.api.Assertions.*;

import com.provys.common.datatype.DtUid;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

class ProvysObjectValueTest {

  private static class TestProvysObjectValue extends ProvysObjectValue {

    TestProvysObjectValue(DtUid id) {
      super(id);
    }
  }

  void getIdTest() {
    var idValue5 = DtUid.valueOf("5");
    assertThat(new TestProvysObjectValue(idValue5).getId()).isEqualTo(idValue5);
  }

  static Stream<@Nullable Object[]> equalsTest() {
    return Stream.of(
        new @Nullable Object[]{new TestProvysObjectValue(DtUid.valueOf("5")),
            new TestProvysObjectValue(DtUid.valueOf("5")), true}
        , new @Nullable Object[]{new TestProvysObjectValue(DtUid.valueOf("5")), null, false}
        , new @Nullable Object[]{new TestProvysObjectValue(DtUid.valueOf("5")),
            new TestProvysObjectValue(DtUid.valueOf("6")), false}
        , new @Nullable Object[]{new TestProvysObjectValue(DtUid.valueOf("5")), "xxx", false}
    );
  }

  @ParameterizedTest
  @MethodSource
  void equalsTest(ProvysObjectValue value, @Nullable Object other, boolean result) {
    assertThat(value.equals(other)).isEqualTo(result);
  }

  static Stream<Object[]> hashCodeTest() {
    return Stream.of(
        new Object[]{new TestProvysObjectValue(DtUid.valueOf("5")),
            new TestProvysObjectValue(DtUid.valueOf("5")), true}
        , new Object[]{new TestProvysObjectValue(DtUid.valueOf("5")),
            new TestProvysObjectValue(DtUid.valueOf("6")), false}
    );
  }

  @ParameterizedTest
  @MethodSource
  void hashCodeTest(ProvysObjectValue value, ProvysObjectValue other, boolean same) {
    if (same) {
      assertThat(value.hashCode()).isEqualTo(other.hashCode());
    } else {
      assertThat(value.hashCode()).isNotEqualTo(other.hashCode());
    }
  }
}