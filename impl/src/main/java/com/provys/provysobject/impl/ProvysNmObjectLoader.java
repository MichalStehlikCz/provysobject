package com.provys.provysobject.impl;

import com.provys.provysobject.ProvysNmObject;
import java.util.Optional;

/**
 * Ancestor for loaders, used to load objects with internal name used as natural key.
 *
 * @param <O> is object interface
 * @param <V> is corresponding value object
 * @param <P> is proxy, enclosing value object
 * @param <M> is repository for given object
 */
public interface ProvysNmObjectLoader<O extends ProvysNmObject, V extends ProvysNmObjectValue,
    P extends ProvysNmObjectProxy<O, V>, M extends ProvysNmObjectManagerInt<O, V, P>>
    extends ProvysObjectLoader<O, V, P, M> {

  /**
   * Load object, identified by supplied internal name.
   *
   * @param manager is manager for type O
   * @param nameNm  is internal name identifying object
   * @return object if found, empty {@code Optional} if not found
   */
  Optional<O> loadByNameNm(M manager, String nameNm);
}
