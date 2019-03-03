package com.provys.provysobject.index;

import com.provys.provysobject.impl.ProvysObjectProxy;
import com.provys.provysobject.impl.ProvysObjectValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class IndexUnique<V extends ProvysObjectValue, P extends ProvysObjectProxy<?, V>, C> implements Index<V, P> {

    private static final Logger LOG = LogManager.getLogger(IndexUnique.class);

    private final String name;
    private final Map<C, P> map;
    private final Function<V, C> attrFunction;

    public IndexUnique(String name, Function<V, C> attrFunction, int initialCapacity) {
        this.name = Objects.requireNonNull(name);
        this.map = new ConcurrentHashMap<>(initialCapacity);
        this.attrFunction = Objects.requireNonNull(attrFunction);
    }

    private void add(P proxy, C attrValue) {
            var old = map.put(attrValue, proxy);
            if (old != null) {
                // this is not completely kosher - while usually we remove from index old object that should have been
                // updated and nce it gets updated, it will be ok, there is risk that even though we are in process of
                // loading value, we were delayed and other thread got ahead of us and we now remove newer value.
                // We would probably need to keep COMMITSCN to be able to resolve which object is newer and discard
                // older one...
                LOG.warn("Conflict with value {} in unique index {}, old object Id {}, new {}", attrValue,
                        name, old.getId(), proxy.getId());
                old.discardValueObject();
            }
    }

    private void remove(P proxy, C attrValue) {
        if (!map.remove(attrValue, proxy)) {
            LOG.warn("Failed to remove value {} from index {} - not present or associated with different proxy",
                    attrValue, name);
        }
    }

    @Override
    public void update(P proxy, @Nullable V oldValue, @Nullable V newValue) {
        C oldAttrValue = attrFunction.apply(oldValue);
        C newAttrValue = attrFunction.apply(newValue);
        if (oldAttrValue != newAttrValue) {
            if (oldAttrValue != null) {
                remove(proxy, oldAttrValue);
            }
            if (newAttrValue != null) {
                add(proxy, newAttrValue);
            }
        }
    }

    @Override
    public void unknownUpdate() {
        // index is still usable because it is used for unique look-ups, no action needed
    }
}
