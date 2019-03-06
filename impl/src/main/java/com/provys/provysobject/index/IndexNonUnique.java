package com.provys.provysobject.index;

import com.provys.common.exception.InternalException;
import com.provys.provysobject.impl.ProvysObjectProxy;
import com.provys.provysobject.impl.ProvysObjectValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Index is used to retrieve list of objects with given value of indexed property. It only makes sense to index values
 * if all values are loaded in memory - otherwise database lookup will still be needed and thus it doesn't make sense
 * to use index...
 */
public final class IndexNonUnique<V extends ProvysObjectValue, P extends ProvysObjectProxy<?, V>, C> extends IndexBase<V, P>
        implements Index<V, P> {

    private static final Logger LOG = LogManager.getLogger(IndexNonUnique.class);

    private final Function<V, C> attrFunction;
    private final Map<C, Map<BigInteger, P>> map;

    public IndexNonUnique(String name, Function<V, C> attrFunction) {
        super(name);
        this.attrFunction = Objects.requireNonNull(attrFunction);
        this.map = new ConcurrentHashMap<>(1);
    }

    private void addToAttrValue(P proxy, C attrValue) {
        var valueMap = map.get(attrValue);
        if ((valueMap != null) && (valueMap.putIfAbsent(proxy.getId(), proxy) != null)) {
            throw new InternalException(LOG, "Found duplicate Id value in index " + getName() + ", id "
                    + proxy.getId());
        }
    }

    private void removeFromAttrValue(P proxy, C attrValue) {
        var valueMap = map.get(attrValue);
        if ((valueMap != null) && !valueMap.remove(proxy.getId(), proxy)) {
            LOG.warn("Removing item {} from index {}, but it is missing", proxy.getId(), getName());
        }
    }

    private void discardAttrValue(C attrValue) {
        // we do not care, if map previously contained item or not
        map.remove(attrValue);
    }

    @Override
    public void update(P proxy, @Nullable V oldValue, @Nullable V newValue) {
        var oldAttrValue = (oldValue == null) ? null : attrFunction.apply(oldValue);
        var newAttrValue = (newValue == null) ? null : attrFunction.apply(newValue);
        if (oldAttrValue != newAttrValue) {
            // only do something if something changed...
            if (oldAttrValue != null) {
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
     * Retrieve collection of objects, associated with given attribute value; returns empty optional if value is not
     * cached
     *
     * @param attrValue is value being searched
     * @return proxy object associated with value, empty optional if suc h object does not exist
     */
    @Nonnull
    public Optional<Collection<P>> get(C attrValue) {
        return Optional.ofNullable(map.get(attrValue)).map(Map::values).map(Collections::unmodifiableCollection);
    }

    /**
     * Add collection of objects to given key
     */
    public void set(C attrValue, Collection<P> values) {
        if (map.put(attrValue, values.stream().
                collect(Collectors.toConcurrentMap(ProvysObjectProxy::getId, Function.identity()))) != null) {
            LOG.warn("Replacing key for value {} in non-unique index {}", attrValue, getName());
        }
    }
}
