package com.provys.provysobject.index;

import com.provys.provysobject.impl.ProvysObjectProxy;
import com.provys.provysobject.impl.ProvysObjectValue;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * In-memory index for indexing proxies and their values. Defines methods for value retrieval and
 * index maintenance.
 *
 * @param <V> is value type
 * @param <P> is its corresponding proxy
 */
public interface Index<V extends ProvysObjectValue, P extends ProvysObjectProxy<?, V>> {

  /**
   * Index name.
   *
   * @return name of index (used to identify index in logs)
   */
  String getName();

  /**
   * Called when proxy value changes. Marks group that originally contained proxy as incomplete.
   * Used both on update and on load / unload of data to cache
   *
   * @param proxy    is proxy object whose value is being changed
   * @param oldValue is old value associated with proxy
   * @param newValue is new value associated with proxy
   */
  void update(P proxy, @Nullable V oldValue, @Nullable V newValue);

  /**
   * Called when object is deleted or it no longer belongs to given sub-index as its position of
   * index has been changed. Completeness of data is not affected (unlike when new value would be
   * changed to null using update). Indices do not maintain data for not loaded items, thus if old
   * value is null, notification is not sent to index
   *
   * @param proxy is proxy object being unregistered
   * @param value is value associated with proxy object
   */
  void delete(P proxy, V value);

  /**
   * Called when summary information is invalidated - e.g. we got notification about change in
   * database but we did not imported all new objects and their current values or when values are
   * removed from proxy or proxy is released to save space. Any further queries using unique id will
   * be still performed against index first and only misses go against database, but any non-unique
   * queries go to database first and thus indices supporting only such searches (e.g. on non-unique
   * value) make no sense and should be discarded.
   */
  void unknownUpdate();
}
