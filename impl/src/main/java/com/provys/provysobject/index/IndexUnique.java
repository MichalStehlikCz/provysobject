package com.provys.provysobject.index;

import com.provys.provysobject.impl.ProvysObjectProxy;
import com.provys.provysobject.impl.ProvysObjectValue;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Unique index can be used for lookup of item based on indexed attribute value.
 *
 * @param <V> is object value class
 * @param <P> is proxy class, corresponding to value class V
 * @param <C> is class representing values of indexed attribute
 */
public final class IndexUnique<V extends ProvysObjectValue, P extends ProvysObjectProxy<?, V>, C>
    extends IndexBase<V, P> {

  private static final Logger LOG = LogManager.getLogger(IndexUnique.class);

  private final Function<V, C> attrFunction;
  private final Map<C, P> map;

  /**
   * Create unique index with specified name and extraction function.
   *
   * @param name is index name
   * @param attrFunction is function that extracts key attribute value from value object
   * @param initialCapacity is expected initial capacity
   */
  public IndexUnique(String name, Function<V, C> attrFunction, int initialCapacity) {
    super(name);
    this.attrFunction = Objects.requireNonNull(attrFunction);
    this.map = new ConcurrentHashMap<>(initialCapacity);
  }

  /**
   * Add value to index. Internal method that takes actual value of indexed property as input
   *
   * @param proxy     is indexed proxy object
   * @param attrValue is value to be indexed
   */
  private void addAttrValue(P proxy, @NonNull C attrValue) {
    var old = map.put(attrValue, proxy);
    if (old != null) {
      // this is not completely kosher - while usually we remove from index old object that should
      // have been updated and once it gets updated, it will be ok (we will just get warning that
      // it was not found), there is risk that even though we are in process of loading value, we
      // were delayed and other thread got ahead of us and we now remove newer value.
      // We would probably need to keep COMMITSCN on value to be able to resolve which object is
      // newer and discard older one... but we do not have one now
      LOG.warn("Conflict with value {} in unique index {}, old object Id {}, new {}", attrValue,
          getName(), old.getId(), proxy.getId());
      old.discardValueObject();
    }
  }

  /**
   * Remove value from index. Internal method that takes actual value of indexed property as input
   *
   * @param proxy     is indexed proxy object
   * @param attrValue is value to be removed
   */
  private void removeAttrValue(P proxy, @NonNull C attrValue) {
    if (!map.remove(attrValue, proxy)) {
      LOG.warn(
          "Failed to remove value {} from index {} - "
              + "not present or associated with different proxy", attrValue, getName());
    }
  }

  @Override
  public void update(P proxy, @Nullable V oldValue, @Nullable V newValue) {
    C oldAttrValue = (oldValue == null) ? null : attrFunction.apply(oldValue);
    C newAttrValue = (newValue == null) ? null : attrFunction.apply(newValue);
    if (!Objects.equals(oldAttrValue, newAttrValue)) {
      if (oldAttrValue != null) {
        removeAttrValue(proxy, oldAttrValue);
      }
      if (newAttrValue != null) {
        addAttrValue(proxy, newAttrValue);
      }
    }
  }

  @Override
  public void delete(P proxy, V value) {
    C attrValue = attrFunction.apply(Objects.requireNonNull(value));
    if (attrValue != null) {
      removeAttrValue(proxy, attrValue);
    }
  }

  @Override
  public void unknownUpdate() {
    // index is still usable because it is used for unique look-ups, no action needed
  }

  /**
   * Retrieve (proxy) object, associated with given attribute value. This is the method index is
   * created for ;)
   *
   * @param attrValue is value being searched
   * @return proxy object associated with value, empty optional if such object does not exist
   */
  public Optional<P> getOpt(@NonNull C attrValue) {
    return Optional.ofNullable(map.get(attrValue));
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    IndexUnique<?, ?, ?> that = (IndexUnique<?, ?, ?>) o;
    return attrFunction.equals(that.attrFunction)
        && map.equals(that.map);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + attrFunction.hashCode();
    result = 31 * result + map.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "IndexUnique{"
        + "attrFunction=" + attrFunction
        + ", map=" + map
        + ", " + super.toString() + '}';
  }
}
