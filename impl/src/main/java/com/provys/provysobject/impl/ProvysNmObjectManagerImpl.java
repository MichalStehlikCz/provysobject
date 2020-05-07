package com.provys.provysobject.impl;

import com.provys.common.exception.RegularException;
import com.provys.provysobject.ProvysNmObject;
import com.provys.provysobject.ProvysRepository;
import com.provys.provysobject.index.Index;
import com.provys.provysobject.index.IndexUnique;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Base class for implementing repository for objects, using internal name as natural key.
 *
 * @param <R> is repository this manager is part of
 * @param <O> is interface representing object
 * @param <V> is value used to hold property values
 * @param <P> is proxy used to access object values
 * @param <M> is manager implementing repository
 * @param <L> is loader used to load data to manager
 */
public abstract class ProvysNmObjectManagerImpl<R extends ProvysRepository,
    O extends ProvysNmObject, V extends ProvysNmObjectValue, P extends ProvysNmObjectProxy<O, V>,
    M extends ProvysNmObjectManagerInt<O, V, P>, L extends ProvysNmObjectLoader<O, V, P, M>>
    extends ProvysObjectManagerImpl<R, O, V, P, M, L> implements ProvysNmObjectManagerInt<O, V, P> {

  private static final String NM_INDEX_NAME = "NmObjectByNameNm";

  private final IndexUnique<V, P, String> provysObjectByNameNm;

  /**
   * Create new object internal repository. Create index by internal name.
   *
   * @param repository      is repository this manager is part of
   * @param loader          is object loader used to populate this manager
   * @param initialCapacity is initial capacity of this repository (estimated number of objects)
   * @param indices         is number of indices in child classes (excluding index by internal
   *                        name)
   */
  // Needed because of addIndex invocation from constructor; it would be better to add
  // @UnderInitialization annotation to method, unfortunately it leads to some internal exception
  // in mockito when trying to mock children of this class
  @SuppressWarnings("Nullness")
  public ProvysNmObjectManagerImpl(R repository, L loader, int initialCapacity, int indices) {
    super(repository, loader, initialCapacity, indices + 1); // +1 for nm index
    provysObjectByNameNm = new IndexUnique<>(NM_INDEX_NAME, ProvysNmObjectValue::getNameNm,
        initialCapacity);
    addIndex(provysObjectByNameNm);
  }

  @Override
  public O getByNameNm(String nameNm) {
    return getOptionalByNameNm(nameNm).orElseThrow(() -> new RegularException(
        "JAVA_MANAGER_OBJECT_NOT_FOUND_BY_NM",
        getEntityNm() + " was not found by supplied internal name " + nameNm,
        Map.of("ENTITY_NM", getEntityNm(), "NAME_NM", nameNm)));
  }

  @Override
  public Optional<O> getOptionalByNameNm(String nameNm) {
    var provysObject = provysObjectByNameNm.getOpt(nameNm)
        .map(ProvysObjectProxy::selfAsObject);
    if (provysObject.isEmpty()) {
      return getLoader().loadByNameNm(self(), nameNm);
    }
    return provysObject;
  }

  @Override
  public @Nullable O getNullableByNameNm(String nameNm) {
    return getOptionalByNameNm(nameNm).orElse(null);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProvysNmObjectManagerImpl<?, ?, ?, ?, ?, ?> that =
        (ProvysNmObjectManagerImpl<?, ?, ?, ?, ?, ?>) o;
    return provysObjectByNameNm.equals(that.provysObjectByNameNm);
  }

  @Override
  public int hashCode() {
    return provysObjectByNameNm.hashCode();
  }

  @Override
  public String toString() {
    return "ProvysNmObjectManagerImpl{"
        + "provysObjectByNameNm=" + provysObjectByNameNm
        + ", " + super.toString() + '}';
  }
}