package com.provys.provysobject.index;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class IdNameNmPairTest {

    @Test
    void getIdTest() {
        assertThat(new IdNameNmPair(BigInteger.valueOf(32), "NM").getId()).isEqualTo(BigInteger.valueOf(32));
    }

    @Test
    void getNameNmTest() {
        assertThat(new IdNameNmPair(BigInteger.valueOf(32), "NM").getNameNm()).isEqualTo("NM");
    }

    @Nonnull
    static Stream<Object[]> equalsTest() {
        return Stream.of(
                new Object[]{new IdNameNmPair(BigInteger.valueOf(5), "NM"),
                        new IdNameNmPair(BigInteger.valueOf(5), "NM"), true}
                , new Object[]{new IdNameNmPair(BigInteger.valueOf(5), "NM"), null, false}
                , new Object[]{new IdNameNmPair(BigInteger.valueOf(5), "NM"),
                        new IdNameNmPair(BigInteger.valueOf(6), "NM"), false}
                , new Object[]{new IdNameNmPair(BigInteger.valueOf(5), "NM"),
                        new IdNameNmPair(BigInteger.valueOf(5), "NN"), false}
                , new Object[]{new IdNameNmPair(BigInteger.valueOf(5), "NM"), "xxx", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void equalsTest(IdNameNmPair value, @Nullable Object other, boolean result) {
        assertThat(value.equals(other)).isEqualTo(result);
    }


    @Test
    void hashCodeTest() {
        assertThat(new IdNameNmPair(BigInteger.valueOf(32), "NM").hashCode()).
                isEqualTo(new IdNameNmPair(BigInteger.valueOf(32), "NM").hashCode());
    }

    @Test
    void toStringTest() {
        assertThat(new IdNameNmPair(BigInteger.valueOf(4), "NM").toString()).startsWith("IdNameNmPair{").
                contains("id=4").contains("nameNm='NM'");
    }
}