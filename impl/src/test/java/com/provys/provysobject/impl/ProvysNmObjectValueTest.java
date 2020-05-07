package com.provys.provysobject.impl;

import static org.assertj.core.api.Assertions.*;

import com.provys.common.datatype.DtUid;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ProvysNmObjectValueTest {

  private static class TestNmProvysObjectValue extends ProvysNmObjectValue {

    TestNmProvysObjectValue(DtUid id, String nameNm) {
      super(id, nameNm);
    }
  }

  @Test
  void getNameNmTest() {
    assertThat(new TestNmProvysObjectValue(DtUid.valueOf("3"), "NM").getNameNm()).isEqualTo("NM");
  }

  static Stream<@Nullable Object[]> equalsTest() {
    return Stream.of(
        new @Nullable Object[]{new TestNmProvysObjectValue(DtUid.valueOf("5"), "NM"),
            new TestNmProvysObjectValue(DtUid.valueOf("5"), "NM"), true}
        , new @Nullable Object[]{new TestNmProvysObjectValue(DtUid.valueOf("5"), "NM1"),
            new TestNmProvysObjectValue(DtUid.valueOf("5"), "NM2"), false}
        , new @Nullable Object[]{new TestNmProvysObjectValue(DtUid.valueOf("5"), "NM"), null, false}
        , new @Nullable Object[]{new TestNmProvysObjectValue(DtUid.valueOf("5"), "NM"),
            new TestNmProvysObjectValue(DtUid.valueOf("6"), "NM"), false}
        ,
        new @Nullable Object[]{new TestNmProvysObjectValue(DtUid.valueOf("5"), "NM"), "xxx", false}
    );
  }

  @ParameterizedTest
  @MethodSource
  void equalsTest(ProvysObjectValue value, @Nullable Object other, boolean result) {
    assertThat(value.equals(other)).isEqualTo(result);
  }

  static Stream<Object[]> hashCodeTest() {
    return Stream.of(
        new Object[]{new TestNmProvysObjectValue(DtUid.valueOf("5"), "NM"),
            new TestNmProvysObjectValue(DtUid.valueOf("5"), "NM"), true}
        , new Object[]{new TestNmProvysObjectValue(DtUid.valueOf("5"), "NM"),
            new TestNmProvysObjectValue(DtUid.valueOf("6"), "NM"), false}
    );
  }

  @ParameterizedTest
  @MethodSource
  void hashCodeTest(ProvysNmObjectValue value, ProvysNmObjectValue other, boolean same) {
    if (same) {
      assertThat(value.hashCode()).isEqualTo(other.hashCode());
    } else {
      assertThat(value.hashCode()).isNotEqualTo(other.hashCode());
    }
  }
}