package com.provys.provysobject.impl;

import com.provys.provysobject.ProvysObject;

/**
 * Object proxy interface - ancestor for proxy, giving access to object data with option to lazy
 * load or release these data.
 *
 * @param <O> is object interface
 * @param <V> is value object for given type
 */
public interface ProvysObjectProxy<O extends ProvysObject, V extends ProvysObjectValue> extends
    ProvysObject {

  /**
   * Reference to self, retyped to object interface.
   *
   * @return reference to itself, retyped to indicate it fulfills contract of object subclass O
   */
  O selfAsObject();

  /**
   * Timestamp when proxy has been last refreshed or used.
   *
   * @return timestamp when proxy was last refreshed / used
   */
  long getLastUsed();

  /**
   * Set last used timestamp. Can be used when looking for items that can be released, because they
   * has not been used for a long time
   */
  void setLastUsed();

  /**
   * Set value object in this proxy. Method should only be invoked by appropriate loader or updater
   * (but must be published as loader is often implemented in different package)
   *
   * @param valueObject is value object to be assigned to proxy
   */
  void setValueObject(V valueObject);

  /**
   * Remove value object from this proxy. Method is usually invoked from cache when it finds
   * conflict on unique key and thus assumes that data are not up-to-date
   */
  void discardValueObject();

  /**
   * Mark proxy as invalid - method used when object is removed from database. Proxy disconnects
   * itself from object manager, releases value object and will throw an exception if accessed
   */
  void deleted();

}
