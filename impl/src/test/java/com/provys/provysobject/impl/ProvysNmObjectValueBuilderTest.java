package com.provys.provysobject.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class ProvysNmObjectValueBuilderTest {

    @Test
    void applyTest() {
        var builder = new TestNmObjectValueBuilder();
        builder.apply(new TestNmObjectValue(BigInteger.valueOf(10), "NM", "Test"));
        assertThat(builder.getNameNm()).isEqualTo("NM");
        builder.apply(new TestNmObjectValue(BigInteger.valueOf(10), "TEST_NM", "Test"));
        assertThat(builder.getNameNm()).isEqualTo("NM");
    }

    @Test
    void notChangedTest() {
        var builder = new TestNmObjectValueBuilder();
        var value = new TestNmObjectValue(BigInteger.valueOf(10), "NM", "Test");
        assertThat(builder.notChanged(value)).isTrue();
        builder.setId(BigInteger.valueOf(10));
        assertThat(builder.notChanged(value)).isTrue();
        builder.setId(BigInteger.valueOf(20));
        assertThat(builder.notChanged(value)).isFalse();
        builder.setId(BigInteger.valueOf(10))
                .setNameNm("TEST");
        assertThat(builder.notChanged(value)).isFalse();
        builder.setNameNm("NM");
        assertThat(builder.notChanged(value)).isTrue();
    }

    @Test
    void testToStringTest() {
        assertThat(new TestNmObjectValueBuilder().setId(BigInteger.valueOf(10)).setNameNm("Name Nm").toString())
                .isEqualTo("ProvysNmObjectValueBuilder{nameNm='Name Nm'} ProvysObjectValueBuilder{id=10}");
    }
}