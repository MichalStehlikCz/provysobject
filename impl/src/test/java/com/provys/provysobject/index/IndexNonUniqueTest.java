package com.provys.provysobject.index;

import com.provys.common.datatype.DtUid;
import com.provys.provysobject.impl.TestNmObjectProxyImpl;
import com.provys.provysobject.impl.TestNmObjectValue;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IndexNonUniqueTest {

    @Test
    void getTest() {
        var proxy1 = mock(TestNmObjectProxyImpl.class);
        var proxy2 = mock(TestNmObjectProxyImpl.class);
        var proxy3 = mock(TestNmObjectProxyImpl.class);
        var textValue1 = "Test";
        var value1_1 = new TestNmObjectValue(DtUid.valueOf("1"), "NAME1", textValue1);
        var value1_2 = new TestNmObjectValue(DtUid.valueOf("2"), "NAME2", textValue1);
        var value1_3 = new TestNmObjectValue(DtUid.valueOf("3"), "NAME3", textValue1);
        var textValue2 = "Test2";
        var value2_1 = new TestNmObjectValue(DtUid.valueOf("1"), "NAME1", textValue2);
        var value2_2 = new TestNmObjectValue(DtUid.valueOf("2"), "NAME2", textValue2);
        var value2_3 = new TestNmObjectValue(DtUid.valueOf("3"), "NAME3", textValue2);
        var indexName = "testIndex";
        var index = new IndexNonUnique<TestNmObjectValue, TestNmObjectProxyImpl, String>(indexName,
                val -> val.getValue().orElse(null));
        // initial - empty
        assertThat(index.get(textValue1)).isEmpty();
        assertThat(index.get(textValue2)).isEmpty();
        // change to value - does nothing as data are still not complete
        index.update(proxy1, null, value1_1);
        assertThat(index.get(textValue1)).isEmpty();
        assertThat(index.get(textValue2)).isEmpty();
        // verify no problem when value is removed (even though it hasn't been registered)
        index.update(proxy1, value1_1, null);
        assertThat(index.get(textValue1)).isEmpty();
        assertThat(index.get(textValue2)).isEmpty();
        // set value first string
        when(proxy1.getId()).thenReturn(DtUid.valueOf("1"));
        when(proxy2.getId()).thenReturn(DtUid.valueOf("2"));
        index.set(textValue1, List.of(proxy1, proxy2));
        assertThat(index.get(textValue1).orElseThrow(() -> new RuntimeException("Collection expected"))).
                containsExactlyInAnyOrder(proxy1, proxy2);
        assertThat(index.get(textValue2)).isEmpty();
        // delete proxy1
        index.delete(proxy1, value1_1);
        assertThat(index.get(textValue1).orElseThrow(() -> new RuntimeException("Collection expected"))).
                containsExactlyInAnyOrder(proxy2);
        assertThat(index.get(textValue2)).isEmpty();
        // move proxy2 to value2 (which is uninitialized)
        index.update(proxy2, value1_2, value2_2);
        assertThat(index.get(textValue1).orElseThrow(() -> new RuntimeException("Collection expected"))).isEmpty();
        assertThat(index.get(textValue2)).isEmpty();
        // move proxy2 back
        index.update(proxy2, value2_2, value1_2);
        assertThat(index.get(textValue1).orElseThrow(() -> new RuntimeException("Collection expected"))).
                containsExactlyInAnyOrder(proxy2);
        assertThat(index.get(textValue2)).isEmpty();
        // init value 2
        when(proxy1.getId()).thenReturn(DtUid.valueOf("1"));
        index.set(textValue2, List.of(proxy1));
        assertThat(index.get(textValue1).orElseThrow(() -> new RuntimeException("Collection expected"))).
                containsExactlyInAnyOrder(proxy2);
        assertThat(index.get(textValue2).orElseThrow(() -> new RuntimeException("Collection expected"))).
                containsExactlyInAnyOrder(proxy1);
        // move proxy1 to value 1
        index.update(proxy1, value2_1, value1_1);
        assertThat(index.get(textValue1).orElseThrow(() -> new RuntimeException("Collection expected"))).
                containsExactlyInAnyOrder(proxy1, proxy2);
        assertThat(index.get(textValue2).orElseThrow(() -> new RuntimeException("Collection expected"))).
                isEmpty();
        // unregister proxy1
        index.update(proxy1, value1_1, null);
        assertThat(index.get(textValue1)).isEmpty();
        assertThat(index.get(textValue2).orElseThrow(() -> new RuntimeException("Collection expected"))).
                isEmpty();
        // unknown update should unregister all
        index.unknownUpdate();
        assertThat(index.get(textValue1)).isEmpty();
        assertThat(index.get(textValue2)).isEmpty();
    }

}