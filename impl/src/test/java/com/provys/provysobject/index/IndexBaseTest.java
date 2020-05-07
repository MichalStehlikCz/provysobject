package com.provys.provysobject.index;

import static org.assertj.core.api.Assertions.*;

import com.provys.common.exception.InternalException;
import com.provys.provysobject.impl.TestNmObjectProxyImpl;
import com.provys.provysobject.impl.TestNmObjectValue;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;

class IndexBaseTest {

  private static class TestIndexBase extends IndexBase<TestNmObjectValue, TestNmObjectProxyImpl> {

    TestIndexBase(String name) {
      super(name);
    }

    @Override
    public void update(TestNmObjectProxyImpl proxy, @Nullable TestNmObjectValue oldValue,
        @Nullable TestNmObjectValue newValue) {
      throw new InternalException("Not implemented in test class");
    }

    @Override
    public void delete(TestNmObjectProxyImpl proxy, TestNmObjectValue value) {
      throw new InternalException("Not implemented in test class");
    }

    @Override
    public void unknownUpdate() {
      throw new InternalException("Not implemented in  test class");
    }
  }

  @Test
  void getNameTest() {
    var index = new TestIndexBase("test");
    assertThat(index.getName()).isEqualTo("test");
  }

  @Test
  void toStringTest() {
    var index = new TestIndexBase("test2");
    assertThat(index.toString()).startsWith("IndexBase{").contains("name='test2'");
  }
}