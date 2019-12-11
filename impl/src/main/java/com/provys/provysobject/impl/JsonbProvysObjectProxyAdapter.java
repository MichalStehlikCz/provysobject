package com.provys.provysobject.impl;

import com.provys.common.exception.InternalException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.bind.adapter.JsonbAdapter;

/**
 * Generic JSON-B adapter for proxy classes - defers serialisation to value class, throws exception when trying
 * de-serialisation
 *
 * @param <P> is proxy class adapter is used for
 * @param <V> is corresponding value class
 */
public class JsonbProvysObjectProxyAdapter<P extends ProvysObjectProxyImpl<?, V, ?, ?>,
        V extends ProvysObjectValue> implements JsonbAdapter<P, V> {

    private static final Logger LOG = LogManager.getLogger(JsonbProvysObjectProxyAdapter.class);

    @Override
    public V adaptToJson(P proxy) {
        return proxy.validateValueObject();
    }

    @Override
    public P adaptFromJson(V v) {
        throw new InternalException(LOG, "Cannot unmarshal Proxy from JSON");
    }
}
