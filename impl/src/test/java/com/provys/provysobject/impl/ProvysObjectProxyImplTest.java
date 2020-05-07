package com.provys.provysobject.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.provys.common.datatype.DtUid;
import com.provys.common.exception.InternalException;
import com.provys.provysobject.ProvysRepository;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@code ProvysObjectProxyImpl} class via {@code TestObjectProxyImpl}
 */
class ProvysObjectProxyImplTest {

  @Test
  @SuppressWarnings("squid:S2925")
    // just wanted to ensure time have some room for change...
  void getLastUsed() {
    var manager = mock(TestObjectManagerImpl.class);
    var proxy = new TestObjectProxyImpl(manager, DtUid.valueOf("5"));
    long lowerBound = System.currentTimeMillis();
    try {
      Thread.sleep(20);
    } catch (InterruptedException e) {
      throw new InternalException("Interrupted during sleep", e);
    }
    proxy.setLastUsed();
    try {
      Thread.sleep(20);
    } catch (InterruptedException e) {
      throw new InternalException("Interrupted during sleep", e);
    }
    assertThat(proxy.getLastUsed()).isBetween(lowerBound, System.currentTimeMillis());
  }

  @Test
  void getManagerTest() {
    var manager = mock(TestObjectManagerImpl.class);
    assertThat(new TestObjectProxyImpl(manager, DtUid.valueOf("5")).getManager()).isSameAs(manager);
  }

  @Test
  void getValueObject() {
    var manager = mock(TestObjectManagerImpl.class);
    var proxy = new TestObjectProxyImpl(manager, DtUid.valueOf("4"));
    assertThat(proxy.getValueObject()).isNull();
    var value1 = new TestObjectValue(DtUid.valueOf("4"), "VALUE1");
    verify(manager, times(0)).registerUpdate(any(), any(), any());
    proxy.setValueObject(value1);
    verify(manager).registerUpdate(proxy, null, value1);
    var value2 = new TestObjectValue(DtUid.valueOf("4"), "VALUE2");
    proxy.setValueObject(value2);
    verify(manager).registerUpdate(proxy, value1, value2);
    assertThat(proxy.getValueObject()).isSameAs(value2);
    proxy.discardValueObject();
    verify(manager).registerUpdate(proxy, value2, null);
    assertThat(proxy.getValueObject()).isNull();
  }

  @Test
  void deletedTest() {
    var manager = mock(TestObjectManagerImpl.class);
    var proxy = new TestObjectProxyImpl(manager, DtUid.valueOf("4"));
    assertThat(proxy.isDeleted()).isFalse();
    proxy.deleted();
    assertThat(proxy.isDeleted()).isTrue();
    assertThatThrownBy(proxy::getValue).isInstanceOf(InternalException.class)
        .hasMessageContaining("deleted");
    assertThatThrownBy(proxy::getId).isInstanceOf(InternalException.class)
        .hasMessageContaining("deleted");
    assertThatThrownBy(proxy::discardValueObject).isInstanceOf(InternalException.class).
        hasMessageContaining("deleted");
    var value1 = new TestObjectValue(DtUid.valueOf("5"), "test");
    assertThatThrownBy(() -> proxy.setValueObject(value1)).isInstanceOf(InternalException.class).
        hasMessageContaining("deleted");
  }

  @Test
  void validateValueObjectTest() {
    var manager = mock(TestObjectManagerImpl.class);
    var proxy = new TestObjectProxyImpl(manager, DtUid.valueOf("4"));
    assertThatThrownBy(proxy::validateValueObject).isInstanceOf(InternalException.class).
        hasMessageContaining("value is empty");
    var value = new TestObjectValue(DtUid.valueOf("4"), "Test");
    proxy.setValueObject(value);
    assertThat(proxy.validateValueObject()).isEqualTo(value);
  }

  static Stream<@Nullable Object[]> equalsTest() {
    return Stream.of(
        new @Nullable Object[]{
            new TestObjectProxyImpl(mock(TestObjectManagerImpl.class), DtUid.valueOf("5")),
            new TestObjectProxyImpl(mock(TestObjectManagerImpl.class), DtUid.valueOf("5")), true},
        new @Nullable Object[]{
            new TestObjectProxyImpl(mock(TestObjectManagerImpl.class), DtUid.valueOf("5")),
            null, false},
        new @Nullable Object[]{
            new TestObjectProxyImpl(mock(TestObjectManagerImpl.class), DtUid.valueOf("5")),
            new TestObjectProxyImpl(mock(TestObjectManagerImpl.class), DtUid.valueOf("6")), false},
        new @Nullable Object[]{
            new TestObjectProxyImpl(mock(TestObjectManagerImpl.class), DtUid.valueOf("5")),
            "xxx", false}
    );
  }

  @ParameterizedTest
  @MethodSource
  void equalsTest(Object value, @Nullable Object other, boolean result) {
    assertThat(value.equals(other)).isEqualTo(result);
  }

  static Stream<Object[]> hashCodeTest() {
    return Stream.of(
        new Object[]{new TestObjectProxyImpl(mock(TestObjectManagerImpl.class), DtUid.valueOf("5")),
            new TestObjectProxyImpl(mock(TestObjectManagerImpl.class), DtUid.valueOf("5")), true}
        ,
        new Object[]{new TestObjectProxyImpl(mock(TestObjectManagerImpl.class), DtUid.valueOf("5")),
            new TestObjectProxyImpl(mock(TestObjectManagerImpl.class), DtUid.valueOf("6")), false}
    );
  }

  @ParameterizedTest
  @MethodSource
  void hashCodeTest(Object value, Object other, boolean same) {
    if (same) {
      assertThat(value.hashCode()).isEqualTo(other.hashCode());
    } else {
      assertThat(value.hashCode()).isNotEqualTo(other.hashCode());
    }
  }
}