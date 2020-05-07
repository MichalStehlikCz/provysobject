package com.provys.provysobject.index;

import com.provys.provysobject.impl.ProvysObjectProxy;
import com.provys.provysobject.impl.ProvysObjectValue;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Common index ancestor, implementing index name handling.
 *
 * @param <V> is type of kept value
 * @param <P> is proxy, corresponding to kept values
 */
abstract class IndexBase<V extends ProvysObjectValue, P extends ProvysObjectProxy<?, V>>
    implements Index<V, P> {

  private final String name;

  IndexBase(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndexBase<?, ?> indexBase = (IndexBase<?, ?>) o;
    return name.equals(indexBase.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public String toString() {
    return "IndexBase{"
        + "name='" + name + '\''
        + '}';
  }
}
