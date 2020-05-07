package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import com.provys.provysobject.ProvysObject;
import java.util.Optional;

/**
 * Enables load of value to proxy for given entity and registration proxies based on supplied
 * condition. Condition is usually Id, name or all objects of given type
 *
 * @param <V> is value type for given entity; loader acts as factory for these value objects
 * @param <P> is proxy class for given entity
 */
public interface ProvysObjectLoader<O extends ProvysObject, V extends ProvysObjectValue,
    P extends ProvysObjectProxy<O, V>, M extends ProvysObjectManagerInt<O, V, P>> {

  /**
   * Load proxy for object, identified by supplied Id.
   *
   * @param manager is manager that triggered the load
   * @param id      is Id of object to be loaded
   * @return loaded object, empty {@code Optional} if object is not found
   */
  Optional<P> loadById(M manager, DtUid id);

  /**
   * Load current value to supplied proxy.
   *
   * @param manager     is repository owning the proxy
   * @param objectProxy is proxy, whose values should be loaded / refreshed
   */
  void loadValue(M manager, P objectProxy);

  /**
   * Load all objects to manager. Should be used with caution, as it might load big amounts of data
   *
   * @param manager is manager acting as repository for loaded objects
   */
  void loadAll(M manager);
}
