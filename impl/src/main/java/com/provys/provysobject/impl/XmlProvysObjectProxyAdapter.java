package com.provys.provysobject.impl;

import com.provys.common.exception.InternalException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Generic JAXB adapter for proxy classes, adapts them to corresponding value class on serialisation and throws an
 * exception when attempting unmarshalling
 *
 * @param <P> is object proxy class
 * @param <V> is corresponding object value class
 */
public class XmlProvysObjectProxyAdapter<P extends ProvysObjectProxyImpl<?, V, ?, ?>,
        V extends ProvysObjectValue> extends XmlAdapter<V, P> {

    private static final Logger LOG = LogManager.getLogger(XmlProvysObjectProxyAdapter.class);

    @Override
    public P unmarshal(V value) {
        throw new InternalException(LOG, "Cannot unmarshal Proxy from XML");
    }

    @Override
    public V marshal(P proxy) {
        return proxy.validateValueObject();
    }
}
