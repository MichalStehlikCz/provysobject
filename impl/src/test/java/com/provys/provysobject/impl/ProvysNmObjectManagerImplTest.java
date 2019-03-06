package com.provys.provysobject.impl;

import com.provys.common.exception.InternalException;
import com.provys.provysobject.ProvysRepository;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProvysNmObjectManagerImplTest {

    @Test
    void getByNameNmTest() {
        var repository = mock(ProvysRepository.class);
        var loader = spy(new TestNmObjectLoaderImpl());
        var manager = new TestNmObjectManagerImpl(repository, loader);
        var nameNm5 = "NM5";
        var proxy5 = manager.getByNameNm(nameNm5);
        assertThat(proxy5.getNameNm()).isEqualTo(nameNm5);
        assertThat(manager.getByNameNm(nameNm5)).isSameAs(proxy5);
        assertThat(manager.getById(proxy5.getId())).isSameAs(proxy5);
        var idValue1 = BigInteger.valueOf(1);
        var proxy1 = manager.getById(idValue1);
        assertThat(proxy1.getId()).isEqualTo(idValue1);
        assertThat(manager.getById(proxy5.getId())).isSameAs(proxy5);
        assertThat(manager.getByNameNm(proxy1.getNameNm())).isSameAs(proxy1);
        verify(loader, times(1)).loadByNameNm(manager, nameNm5);
        verify(loader, times(1)).loadById(manager, idValue1);
        verify(loader, times(1)).loadByNameNm(any(), any());
        verify(loader, times(1)).loadById(any(), any());
        assertThatThrownBy(() -> manager.getByNameNm("NAME8")).
                hasMessageContaining("JAVA_MANAGER_OBJECT_NOT_FOUND_BY_NM").
                hasMessageContaining("NAME8");
    }

    @Test
    void getByIdIfExistsTest() {
        var repository = mock(ProvysRepository.class);
        var loader = spy(new TestNmObjectLoaderImpl());
        var manager = new TestNmObjectManagerImpl(repository, loader);
        var nameNm5 = "NM5";
        var proxy5 = manager.getByNameNmIfExists(nameNm5).orElseThrow();
        assertThat(proxy5.getNameNm()).isEqualTo(nameNm5);
        assertThat(manager.getByNameNmIfExists(nameNm5)).containsSame(proxy5);
        assertThat(manager.getById(proxy5.getId())).isSameAs(proxy5);
        var idValue1 = BigInteger.valueOf(1);
        var proxy1 = manager.getById(idValue1);
        assertThat(proxy1.getId()).isEqualTo(idValue1);
        assertThat(manager.getById(proxy5.getId())).isSameAs(proxy5);
        assertThat(manager.getByNameNmIfExists(proxy1.getNameNm())).containsSame(proxy1);
        verify(loader, times(1)).loadByNameNm(manager, nameNm5);
        verify(loader, times(1)).loadById(manager, idValue1);
        verify(loader, times(1)).loadByNameNm(any(), any());
        verify(loader, times(1)).loadById(any(), any());
        assertThat(manager.getByNameNmIfExists("NAME8")).isEmpty();
    }

    @Test
    void registerChange() {
        var repository = mock(ProvysRepository.class);
        var loader = spy(new TestNmObjectLoaderImpl());
        var manager = new TestNmObjectManagerImpl(repository, loader);
        var proxy5 = manager.getOrAddById(BigInteger.valueOf(5));
        var value5 = new TestNmObjectValue(BigInteger.valueOf(5), "NM5", "text");
        manager.registerUpdate(proxy5,  null, value5);
        assertThat(manager.getByNameNm("NM5")).isSameAs(proxy5);
        verify(loader, times(0)).loadByNameNm(any(), any());// should be in index after register
        manager.registerUpdate(proxy5, value5, null);
        assertThat(manager.getByNameNm("NM5")).isSameAs(proxy5); // loader should match it by id
        verify(loader, times(1)).loadByNameNm(any(), any()); // value was not in index and had
                                                                                     // to be loaded
        manager.unregister(proxy5, proxy5.getValueObject().orElse(null));
        assertThat(manager.getByNameNm("NM5")).isNotSameAs(proxy5).isEqualTo(proxy5); // after un-registration, we
                                      // should get new proxy, but this proxy will be initialized to same id in loader
        assertThatThrownBy(() -> manager.registerUpdate(proxy5, null, null)).
                isInstanceOf(InternalException.class); // should fail as proxy5 is no longer registered
    }
}
