package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class ProvysObjectValueBuilderTest {

    @Test
    void newFromValueTest() {
        var value = new TestObjectValue(DtUid.valueOf("10"), "Test");
        assertThat(new TestObjectValueBuilder(value)).isEqualTo(
                new TestObjectValueBuilder().setId(DtUid.valueOf("10")).setValue("Test"));
    }

    @Test
    void newFromBuilderTest() {
        var builder = new TestObjectValueBuilder().setId(DtUid.valueOf("10")).setValue("Test");
        assertThat(new TestObjectValueBuilder(builder)).isEqualTo(builder);
    }

    @Test
    void setIdTest() {
        var builder = new TestObjectValueBuilder().setId(DtUid.valueOf("10"));
        assertThat(builder.getId()).isEqualTo(DtUid.valueOf("10"));
        builder.setId(DtUid.valueOf("20"));
        assertThat(builder.getId()).isEqualTo(DtUid.valueOf("20"));
        builder.setId(null);
        assertThat(builder.getId()).isNull();
    }

    @Test
    void applyTest() {
        var builder = new TestObjectValueBuilder();
        builder.apply(new TestObjectValue(DtUid.valueOf("10"), "Test"));
        assertThat(builder.getId()).isEqualTo(DtUid.valueOf("10"));
        builder.apply(new TestObjectValue(DtUid.valueOf("20"), "Test"));
        assertThat(builder.getId()).isEqualTo(DtUid.valueOf("10"));
    }

    @Test
    void notChangedTest() {
        var builder = new TestObjectValueBuilder();
        var value = new TestObjectValue(DtUid.valueOf("10"), "Test");
        assertThat(builder.notChanged(value)).isTrue();
        builder.setId(DtUid.valueOf("10"));
        assertThat(builder.notChanged(value)).isTrue();
        builder.setId(DtUid.valueOf("20"));
        assertThat(builder.notChanged(value)).isFalse();
    }

    @Test
    void buildFromTest() {
        var builder = new TestObjectValueBuilder();
        var value = new TestObjectValue(DtUid.valueOf("10"), "Test");
        assertThat(builder.buildFrom(value)).isSameAs(value);
        assertThat(builder.getId()).isNull();
        builder.setId(DtUid.valueOf("10"));
        assertThat(builder.buildFrom(value)).isSameAs(value);
        builder.setId(DtUid.valueOf("20"));
        assertThat(builder.buildFrom(value).getId()).isEqualTo(DtUid.valueOf("20"));
    }

    @Test
    void testToStringTest() {
        assertThat(new TestObjectValueBuilder().setId(DtUid.valueOf("10")).toString())
                .isEqualTo("ProvysObjectValueBuilder{id=ID10}");
    }
}