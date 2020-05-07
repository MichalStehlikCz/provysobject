package com.provys.provysobject.index;

import com.provys.common.datatype.DtUid;
import com.provys.common.exception.InternalException;
import com.provys.provysobject.impl.ProvysObjectProxy;
import com.provys.provysobject.impl.ProvysObjectValue;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Index is used to retrieve list of objects with given value of indexed property. It only makes
 * sense to index values if all values are loaded in memory - otherwise database lookup will still
 * be needed and thus it doesn't make sense to use index...
 */
public final class IndexNonUnique<V extends ProvysObjectValue, P extends ProvysObjectProxy<?, V>, C>
    extends IndexBase<V, P> {

  private static final Logger LOG = LogManager.getLogger(IndexNonUnique.class);

  private final Function<V, @Nullable C> attrFunction;
  private final Map<C, Map<DtUid, P>> map;

  /**
   * Constructor, creates index based on name and function, extracting unique key from value.
   *
   * @param name         is name of index to be created
   * @param attrFunction is function used to extract value from value object
   */
  public IndexNonUnique(String name, Function<V, @Nullable C> attrFunction) {
    super(name);
    this.attrFunction = Objects.requireNonNull(attrFunction);
    this.map = new ConcurrentHashMap<>(1);
  }

  private void addToAttrValue(P proxy, @NonNull C attrValue) {
    var valueMap = map.get(attrValue);
    if ((valueMap != null) && (valueMap.putIfAbsent(proxy.getId(), proxy) != null)) {
      throw new InternalException(
          "Found duplicate Id value in index " + getName() + ", id " + proxy.getId());
    }
  }

  private void removeFromAttrValue(P proxy, @NonNull C attrValue) {
    Map<DtUid, P> valueMap = map.get(attrValue);
    if ((valueMap != null) && !valueMap.remove(proxy.getId(), proxy)) {
      LOG.warn("Removing item {} from index {}, but it is missing", proxy.getId(), getName());
    }
  }

  private void discardAttrValue(@NonNull C attrValue) {
    // we do not care, if map previously contained item or not
    map.remove(attrValue);
  }

  @Override
  public void update(P proxy, @Nullable V oldValue, @Nullable V newValue) {
    var oldAttrValue = (oldValue == null) ? null : attrFunction.apply(oldValue);
    var newAttrValue = (newValue == null) ? null : attrFunction.apply(newValue);
    if (!Objects.equals(oldAttrValue, newAttrValue)) {
      // only do something if something changed...
      if (oldAttrValue != null) {
        //noinspection VariableNotUsedInsideIf - nullness indicates new value is not valid...
        if (newValue == null) {
          // we have to discard given child list, as it is no longer valid...
          discardAttrValue(oldAttrValue);
        } else {
          removeFromAttrValue(proxy, oldAttrValue);
        }
      }
      if (newAttrValue != null) {
        addToAttrValue(proxy, newAttrValue);
      }
    }
  }

  @Override
  public void delete(P proxy, V value) {
    C attrValue = attrFunction.apply(Objects.requireNonNull(value));
    if (attrValue != null) {
      removeFromAttrValue(proxy, attrValue);
    }
  }

  @Override
  public void unknownUpdate() {
    // we do not know if any of the items are full child lists any more...
    map.clear();
  }

  /**
   * Retrieve collection of objects, associated with given attribute value. Returns empty optional
   * if value is not cached
   *
   * @param attrValue is value being searched
   * @return proxy object associated with value, empty optional if such object does not exist
   */
  // empty collection means no values for given key, empty optional means key is not cached
  @SuppressWarnings("OptionalContainsCollection")
  public Optional<Collection<P>> get(@NonNull C attrValue) {
    return Optional.ofNullable(map.get(attrValue)).map(Map::values)
        .map(Collections::unmodifiableCollection);
  }

  /**
   * Add collection of objects to given key.
   *
   * @param attrValue is extracted key value
   * @param values    is list of values that should be assigned to given key
   */
  public void set(@NonNull C attrValue, Collection<? extends P> values) {
    if (map.put(attrValue, values.stream()
        .collect(Collectors.toConcurrentMap(ProvysObjectProxy::getId, Function.identity())))
        != null) {
      LOG.warn("Replacing key for value {} in non-unique index {}", attrValue, getName());
    }
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
    IndexNonUnique<?, ?, ?> that = (IndexNonUnique<?, ?, ?>) o;
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
    return "IndexNonUnique{"
        + "attrFunction=" + attrFunction
        + ", map=" + map
        + ", " + super.toString() + '}';
  }
}
