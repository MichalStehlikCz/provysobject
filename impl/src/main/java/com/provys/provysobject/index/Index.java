package com.provys.provysobject.index;

import com.provys.provysobject.impl.ProvysObjectProxy;
import com.provys.provysobject.impl.ProvysObjectValue;

import javax.annotation.Nullable;

public interface Index<V extends ProvysObjectValue, P extends ProvysObjectProxy<?, V>> {

    /**
     * Called when proxy value changes
     *
     * @param proxy is proxy object whose value is being changed
     * @param oldValue is old value associated with proxy
     * @param newValue is new value associated with proxy
     */
    void update(P proxy, @Nullable V oldValue, @Nullable V newValue);

    /**
     * Called when summary information is invalidated - e.g. we got notification about change in database but we did not
     * imported all new objects and their current values or when values are removed from proxy or proxy is released to
     * save space. An further queries performed unique id will be still performed against index first and only misses go
     * against database, but any non-unique queries go to database first and thus indices supporting only such searches
     * (e.g. on non-unique value) make no sense and should be discarded
     */
    void unknownUpdate();
}
