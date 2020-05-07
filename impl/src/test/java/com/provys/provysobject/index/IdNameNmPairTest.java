package com.provys.provysobject.index;

import static org.assertj.core.api.Assertions.*;

import com.provys.common.datatype.DtUid;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class IdNameNmPairTest {

  @Test
  void getIdTest() {
    assertThat(new IdNameNmPair(DtUid.valueOf("32"), "NM").getId())
        .isEqualTo(DtUid.valueOf("32"));
  }

  @Test
  void getNameNmTest() {
    assertThat(new IdNameNmPair(DtUid.valueOf("32"), "NM").getNameNm()).isEqualTo("NM");
  }

  static Stream<@Nullable Object[]> equalsTest() {
    return Stream.of(
        new @Nullable Object[]{new IdNameNmPair(DtUid.valueOf("5"), "NM"),
            new IdNameNmPair(DtUid.valueOf("5"), "NM"), true}
        , new @Nullable Object[]{new IdNameNmPair(DtUid.valueOf("5"), "NM"), null, false}
        , new @Nullable Object[]{new IdNameNmPair(DtUid.valueOf("5"), "NM"),
            new IdNameNmPair(DtUid.valueOf("6"), "NM"), false}
        , new @Nullable Object[]{new IdNameNmPair(DtUid.valueOf("5"), "NM"),
            new IdNameNmPair(DtUid.valueOf("5"), "NN"), false}
        , new @Nullable Object[]{new IdNameNmPair(DtUid.valueOf("5"), "NM"), "xxx", false}
    );
  }

  @ParameterizedTest
  @MethodSource
  void equalsTest(IdNameNmPair value, @Nullable Object other, boolean result) {
    assertThat(value.equals(other)).isEqualTo(result);
  }


  @Test
  void hashCodeTest() {
    assertThat(new IdNameNmPair(DtUid.valueOf("32"), "NM").hashCode()).
        isEqualTo(new IdNameNmPair(DtUid.valueOf("32"), "NM").hashCode());
  }

  @Test
  void toStringTest() {
    assertThat(new IdNameNmPair(DtUid.valueOf("4"), "NM").toString())
        .startsWith("IdNameNmPair{")
        .contains("id=ID4")
        .contains("nameNm='NM'");
  }
}