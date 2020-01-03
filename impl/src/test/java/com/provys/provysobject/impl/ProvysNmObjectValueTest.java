package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

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

    @Nonnull
    static Stream<Object[]> equalsTest() {
        return Stream.of(
                new Object[]{new TestNmProvysObjectValue(DtUid.valueOf("5"), "NM"),
                        new TestNmProvysObjectValue(DtUid.valueOf("5"), "NM"), true}
                , new Object[]{new TestNmProvysObjectValue(DtUid.valueOf("5"), "NM1"),
                        new TestNmProvysObjectValue(DtUid.valueOf("5"), "NM2"), false}
                , new Object[]{new TestNmProvysObjectValue(DtUid.valueOf("5"), "NM"), null, false}
                , new Object[]{new TestNmProvysObjectValue(DtUid.valueOf("5"), "NM"),
                        new TestNmProvysObjectValue(DtUid.valueOf("6"), "NM"), false}
                , new Object[]{new TestNmProvysObjectValue(DtUid.valueOf("5"), "NM"), "xxx", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void equalsTest(ProvysNmObjectValue value, @Nullable Object other, boolean result) {
        assertThat(value.equals(other)).isEqualTo(result);
    }

    @Nonnull
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