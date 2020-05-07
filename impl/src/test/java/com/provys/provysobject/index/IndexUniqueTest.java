package com.provys.provysobject.index;

import com.provys.common.datatype.DtUid;
import com.provys.provysobject.impl.ProvysNmObjectValue;
import com.provys.provysobject.impl.TestNmObjectProxyImpl;
import com.provys.provysobject.impl.TestNmObjectValue;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class IndexUniqueTest {

  @Test
  void getTest() {
    var proxy = mock(TestNmObjectProxyImpl.class);
    var value = new TestNmObjectValue(DtUid.valueOf("5"), "NAME_NM", "Test");
    var nameNm = "NAME_NM";
    var value2 = new TestNmObjectValue(DtUid.valueOf("5"), "NAME2_NM", "Test2");
    var nameNm2 = "NAME2_NM";
    var index = new IndexUnique<TestNmObjectValue, TestNmObjectProxyImpl, String>("testIndex",
        ProvysNmObjectValue::getNameNm, 10);
    // initial - empty
    assertThat(index.getOpt(nameNm)).isEmpty();
    assertThat(index.getOpt(nameNm2)).isEmpty();
    // change to value
    index.update(proxy, null, value);
    assertThat(index.getOpt(nameNm)).containsSame(proxy);
    assertThat(index.getOpt(nameNm2)).isEmpty();
    // change back to null
    index.update(proxy, value, null);
    assertThat(index.getOpt(nameNm)).isEmpty();
    assertThat(index.getOpt(nameNm2)).isEmpty();
    // change to value and then to value2
    index.update(proxy, null, value);
    index.update(proxy, value, value2);
    assertThat(index.getOpt(nameNm)).isEmpty();
    assertThat(index.getOpt(nameNm2)).containsSame(proxy);
    // delete
    index.delete(proxy, value2);
    assertThat(index.getOpt(nameNm)).isEmpty();
    assertThat(index.getOpt(nameNm2)).isEmpty();
  }

}