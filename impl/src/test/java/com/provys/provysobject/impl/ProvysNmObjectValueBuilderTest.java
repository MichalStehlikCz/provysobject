package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import com.provys.common.exception.InternalException;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.*;

class ProvysNmObjectValueBuilderTest {

    @Test
    void fromValueTest() {
        var builder = new TestNmObjectValueBuilder(
                new TestNmObjectValue(DtUid.valueOf("10"), "NM", "value"));
        assertThat(builder.getNameNm()).isEqualTo("NM");
        assertThat(builder.getUpdNameNm()).isTrue();
    }

    @Test
    void fromBuilderTest() {
        var builder = new TestNmObjectValueBuilder(new TestNmObjectValueBuilder());
        assertThat(builder.getNameNm()).isNull();
        assertThat(builder.getUpdNameNm()).isFalse();
        builder = new TestNmObjectValueBuilder(builder.setNameNm("NM"));
        assertThat(builder.getNameNm()).isEqualTo("NM");
        assertThat(builder.getUpdNameNm()).isTrue();
    }

    @Test
    void setTest() {
        var builder = new TestNmObjectValueBuilder();
        assertThat(builder.getNameNm()).isNull();
        assertThat(builder.getUpdNameNm()).isFalse();
        builder.setNameNm("NM");
        assertThat(builder.getNameNm()).isEqualTo("NM");
        assertThat(builder.getUpdNameNm()).isTrue();
        builder.setUpdNameNm(true);
        assertThat(builder.getNameNm()).isEqualTo("NM");
        builder.setNameNm("TestNm");
        assertThat(builder.getNameNm()).isEqualTo("TestNm");
        assertThat(builder.getUpdNameNm()).isTrue();
        builder.setUpdNameNm(false);
        assertThat(builder.getNameNm()).isNull();
        assertThat(builder.getUpdNameNm()).isFalse();
        assertThatThrownBy(() -> builder.setUpdNameNm(true)).isInstanceOf(InternalException.class);
    }

    @Test
    void applyTest() {
        var builder = new TestNmObjectValueBuilder();
        builder.apply(new TestNmObjectValue(DtUid.valueOf("10"), "NM", "Test"));
        assertThat(builder.getNameNm()).isEqualTo("NM");
        builder.apply(new TestNmObjectValue(DtUid.valueOf("10"), "TEST_NM", "Test"));
        assertThat(builder.getNameNm()).isEqualTo("NM");
    }

    @Test
    void notChangedTest() {
        var builder = new TestNmObjectValueBuilder();
        var value = new TestNmObjectValue(DtUid.valueOf("10"), "NM", "Test");
        assertThat(builder.notChanged(value)).isTrue();
        builder.setId(DtUid.valueOf("10"));
        assertThat(builder.notChanged(value)).isTrue();
        builder.setId(DtUid.valueOf("20"));
        assertThat(builder.notChanged(value)).isFalse();
        builder.setId(DtUid.valueOf("10"))
                .setNameNm("TEST");
        assertThat(builder.notChanged(value)).isFalse();
        builder.setNameNm("NM");
        assertThat(builder.notChanged(value)).isTrue();
    }

    @Test
    void testToStringTest() {
        assertThat(new TestNmObjectValueBuilder()
            .setId(DtUid.valueOf("10"))
            .setNameNm("Name Nm")
            .toString())
                .isEqualTo("TestNmObjectValueBuilder{value='null', updValue=false, "
                    + "ProvysNmObjectValueBuilder{nameNm='Name Nm', "
                    + "ProvysObjectValueBuilder{id=ID10}}}");
    }
}