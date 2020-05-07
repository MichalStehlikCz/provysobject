package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import com.provys.common.exception.InternalException;
import com.provys.provysobject.ProvysRepository;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProvysObjectLoaderImplTest {

  @Test
  void loadByIdTest() {
    var repository = mock(ProvysRepository.class);
    var loader = new TestObjectLoaderImpl();
    var manager = new TestObjectManagerImpl(repository, loader);
    var idValue5 = DtUid.valueOf("5");
    var proxy5 = manager.getOrAddById(idValue5);
    loader.loadById(manager, idValue5);
    assertThat(proxy5.getValue()).isEqualTo("Another test value");
  }

  @Test
  void loadValueTest() {
    var repository = mock(ProvysRepository.class);
    var loader = new TestObjectLoaderImpl();
    var manager = new TestObjectManagerImpl(repository, loader);
    var idValue5 = DtUid.valueOf("5");
    var proxy5 = manager.getOrAddById(idValue5);
    loader.loadValue(manager, proxy5);
    assertThat(proxy5.getValue()).isEqualTo("Another test value");
    var idValue6 = DtUid.valueOf("6");
    var proxy6 = manager.getOrAddById(idValue6);
    loader.loadValue(manager, proxy5);
    assertThatThrownBy(() -> loader.loadValue(manager, proxy6))
        .isInstanceOf(InternalException.class)
        .hasMessageContaining("multiple");
  }

  @Test
  void loadAllTest() {
    var repository = mock(ProvysRepository.class);
    var loader = new TestObjectLoaderImpl();
    var manager = new TestObjectManagerImpl(repository, loader);
    var idValue1 = DtUid.valueOf("1");
    var proxy1 = manager.getOrAddById(idValue1);
    var idValue5 = DtUid.valueOf("5");
    var proxy5 = manager.getOrAddById(idValue5);
    loader.loadAll(manager);
    assertThat(proxy1.getValue()).isEqualTo("Test value");
    assertThat(proxy5.getValue()).isEqualTo("Another test value");
  }
}