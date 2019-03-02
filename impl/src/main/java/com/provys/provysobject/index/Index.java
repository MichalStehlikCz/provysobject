package com.provys.provysobject.index;

import com.provys.provysobject.impl.ProvysObjectProxy;
import com.provys.provysobject.impl.ProvysObjectValue;

import javax.annotation.Nullable;

public interface Index<V extends ProvysObjectValue, P extends ProvysObjectProxy<?, V>> {
    /**
     * Called when new value is being added to indexed table. Indexes value if returned column is non-null
     *
     * @param proxy is proxy to object being indexed
     * @param value is value to be indexed
     */
    void add(P proxy, V value);

    /**
     * Called when value is to be removed from index
     *
     * @param proxy is indexed proxy object
     * @param value is value to be indexed
     */
    void remove(P proxy, V value);

    /**
     * Called when proxy value changes
     *
     * @param proxy is proxy object whose value is being changed
     * @param oldValue is old value associated with proxy
     * @param newValue is new value associated with proxy
     */
    void update(P proxy, @Nullable V oldValue, @Nullable V newValue);
}
