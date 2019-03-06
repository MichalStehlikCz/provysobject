package com.provys.provysobject.impl;

import com.provys.common.exception.InternalException;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProvysObjectLoaderImplTest {

    @Test
    void loadByIdTest() {
        var loader = new TestObjectLoaderImpl();
        var manager = mock(TestObjectManagerImpl.class);
        var proxy5 = mock(TestObjectProxyImpl.class);
        var idValue5 = BigInteger.valueOf(5);
        when(manager.getOrAddById(idValue5)).thenReturn(proxy5);
        loader.loadById(manager, idValue5);
        verify(proxy5).setValueObject(new TestObjectValue(idValue5, "Another test value"));
        verifyNoMoreInteractions(proxy5);
    }

    @Test
    void loadValueTest() {
        var loader = new TestObjectLoaderImpl();
        var manager = mock(TestObjectManagerImpl.class);
        var proxy5 = mock(TestObjectProxyImpl.class);
        var idValue5 = BigInteger.valueOf(5);
        when(proxy5.getId()).thenReturn(idValue5);
        loader.loadValue(manager, proxy5);
        verify(proxy5).getId();
        verify(proxy5).setValueObject(new TestObjectValue(idValue5, "Another test value"));
        verifyNoMoreInteractions(proxy5);
        var proxy6 = mock(TestObjectProxyImpl.class);
        var idValue6 = BigInteger.valueOf(6);
        when(proxy6.getId()).thenReturn(idValue6);
        assertThatThrownBy(() -> loader.loadValue(manager, proxy6)).isInstanceOf(InternalException.class).
                hasMessageContaining("multiple");
    }

    @Test
    void loadAllTest() {
        var loader = new TestObjectLoaderImpl();
        var manager = mock(TestObjectManagerImpl.class);
        var proxy1 = mock(TestObjectProxyImpl.class);
        var idValue1 = BigInteger.valueOf(1);
        when(manager.getOrAddById(idValue1)).thenReturn(proxy1);
        var proxy5 = mock(TestObjectProxyImpl.class);
        var idValue5 = BigInteger.valueOf(5);
        when(manager.getOrAddById(idValue5)).thenReturn(proxy5);
        var proxy6 = mock(TestObjectProxyImpl.class);
        var idValue6 = BigInteger.valueOf(6);
        when(manager.getOrAddById(idValue6)).thenReturn(proxy6);
        loader.loadAll(manager);
        verify(proxy1).setValueObject(new TestObjectValue(idValue1, "Test value"));
        verifyNoMoreInteractions(proxy1);
        verify(proxy5).setValueObject(new TestObjectValue(idValue5, "Another test value"));
        verifyNoMoreInteractions(proxy5);
    }
}