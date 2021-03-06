package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import com.provys.common.exception.InternalException;
import com.provys.provysobject.ProvysRepository;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Uses TestObjectManagerImpl as test implementation.
 */
class ProvysObjectManagerImplTest {

  @Test
  void getRepositoryTest() {
    var repository = mock(ProvysRepository.class);
    var loader = mock(TestObjectLoaderImpl.class);
    var manager = new TestObjectManagerImpl(repository, loader);
    assertThat(manager.getRepository()).isSameAs(repository);
  }

  @Test
  void getLoaderTest() {
    var repository = mock(ProvysRepository.class);
    var loader = mock(TestObjectLoaderImpl.class);
    var manager = new TestObjectManagerImpl(repository, loader);
    assertThat(manager.getLoader()).isSameAs(loader);
  }

  @Test
  void getByIdTest() {
    var repository = mock(ProvysRepository.class);
    var loader = spy(new TestObjectLoaderImpl());
    var manager = new TestObjectManagerImpl(repository, loader);
    var idValue5 = DtUid.valueOf("5");
    var proxy5 = manager.getById(idValue5);
    assertThat(proxy5.getId()).isEqualTo(idValue5);
    assertThat(manager.getById(idValue5)).isSameAs(proxy5);
    var idValue1 = DtUid.valueOf("1");
    var proxy1 = manager.getById(idValue1);
    assertThat(proxy1.getId()).isEqualTo(idValue1);
    assertThat(manager.getById(idValue5)).isSameAs(proxy5);
    assertThat(manager.getById(idValue1)).isSameAs(proxy1);
    verify(loader, times(1)).loadById(manager, idValue5);
    verify(loader, times(1)).loadById(manager, idValue1);
    assertThatThrownBy(() -> manager.getById(DtUid.valueOf("8"))).
        hasMessageContaining("JAVA_MANAGER_OBJECT_NOT_FOUND").
        hasMessageContaining("8");
  }

  @Test
  void getOptionalByIdTest() {
    var repository = mock(ProvysRepository.class);
    var loader = spy(new TestObjectLoaderImpl());
    var manager = new TestObjectManagerImpl(repository, loader);
    var idValue5 = DtUid.valueOf("5");
    var proxy5 = manager.getOptionalById(idValue5).orElseThrow();
    assertThat(proxy5.getId()).isEqualTo(idValue5);
    assertThat(manager.getById(idValue5)).isSameAs(proxy5);
    var idValue1 = DtUid.valueOf("1");
    var proxy1 = manager.getOptionalById(idValue1).orElseThrow();
    assertThat(proxy1.getId()).isEqualTo(idValue1);
    assertThat(manager.getOptionalById(idValue5)).containsSame(proxy5);
    assertThat(manager.getOptionalById(idValue1)).containsSame(proxy1);
    verify(loader, times(1)).loadById(manager, idValue5);
    verify(loader, times(1)).loadById(manager, idValue1);
    assertThat(manager.getOptionalById(DtUid.valueOf("8"))).isEmpty();
  }

  @Test
  void getAllTest() {
    var repository = mock(ProvysRepository.class);
    var loader = new TestObjectLoaderImpl();
    var manager = new TestObjectManagerImpl(repository, loader);
    var idValue1 = DtUid.valueOf("1");
    var idValue5 = DtUid.valueOf("5");
    var idValue6 = DtUid.valueOf("6");
    assertThat(manager.getAll())
        .containsExactlyInAnyOrder(manager.getById(idValue1), manager.getById(idValue5),
            manager.getById(idValue6));
  }

  @Test
  void getOrAddByIdTest() {
    var repository = mock(ProvysRepository.class);
    var loader = new TestObjectLoaderImpl();
    var manager = new TestObjectManagerImpl(repository, loader);
    var idValue5 = DtUid.valueOf("5");
    var proxy5 = manager.getById(idValue5);
    assertThat(manager.getOrAddById(idValue5)).isSameAs(proxy5);
    var idValue8 = DtUid.valueOf("8");
    var proxy8 = manager.getOrAddById(idValue8);
    assertThat(proxy8.getId()).isEqualTo(idValue8);
    assertThat(manager.getOrAddById(idValue8)).isSameAs(proxy8);
  }

  @Test
  void loadValueObjectTest() {
    var repository = mock(ProvysRepository.class);
    var loader = new TestObjectLoaderImpl();
    var manager = new TestObjectManagerImpl(repository, loader);
    var proxy = manager.getOrAddById(DtUid.valueOf("5"));
    assertThat(proxy.getValueObject()).isNull();
    manager.loadValueObject(proxy);
    assertThat(proxy.getValueObject()).isNotNull();
    assertThat(proxy.getValue())
        .isEqualTo("Another test value"); // has to match with TestObjectLoaderImpl
  }

  @Test
  void registerChange() {
    var repository = mock(ProvysRepository.class);
    var loader = new TestObjectLoaderImpl();
    var manager = new TestObjectManagerImpl(repository, loader);
    var proxy5 = manager.getOrAddById(DtUid.valueOf("5"));
    assertThatCode(() -> manager.registerUpdate(proxy5, null, null)).
        doesNotThrowAnyException();
    assertThatCode(() -> manager.registerUpdate(proxy5, null, null)).
        doesNotThrowAnyException();
    manager.unregister(proxy5, null);
    assertThatThrownBy(() -> manager.registerUpdate(proxy5, null, null)).
        isInstanceOf(InternalException.class);
  }

  @Test
  void registerDelete() {
    var repository = mock(ProvysRepository.class);
    var loader = spy(new TestObjectLoaderImpl());
    var manager = new TestObjectManagerImpl(repository, loader);
    var idValue5 = DtUid.valueOf("5");
    var proxy5 = manager.getOrAddById(idValue5);
    manager.getById(idValue5);
    verify(loader, times(0)).loadById(manager, idValue5);
    // registerDelete
    manager.registerDelete(proxy5, null);
    manager.getById(idValue5);
    verify(loader, times(1)).loadById(manager, idValue5);
  }

  @Test
  void unregister() {
    var repository = mock(ProvysRepository.class);
    var loader = spy(new TestObjectLoaderImpl());
    var manager = new TestObjectManagerImpl(repository, loader);
    var idValue5 = DtUid.valueOf("5");
    var proxy5 = manager.getOrAddById(idValue5);
    manager.getById(idValue5);
    verify(loader, times(0)).loadById(manager, idValue5);
    // unregister
    manager.unregister(proxy5, null);
    manager.getById(idValue5);
    verify(loader, times(1)).loadById(manager, idValue5);
  }
}