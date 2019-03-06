package com.provys.provysobject.impl;

import com.provys.provysobject.ProvysObject;
import com.provys.provysobject.ProvysObjectManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;

interface ProvysObjectManagerInt<O extends ProvysObject, V extends ProvysObjectValue,
        P extends ProvysObjectProxy<O, V>>
        extends ProvysObjectManager<O> {

    @Nonnull
    P getOrAddById(BigInteger id);

    /**
     * Ask loader to load values to given proxy. Used by proxy for its own refresh, enables isolation of proxy from
     * loader.
     */
    void loadValueObject(P objectProxy);

    /**
     * Register given object in indices. Verifies that object proxy has been previously registered for its id, if not,
     * throws exception
     *
     * @param objectProxy is proxy to object to be registered
     * @param oldValue are old values associated with object
     * @param newValue are new values associated with object
     */
    void registerUpdate(P objectProxy, @Nullable V oldValue, @Nullable V newValue);

    /**
     * Register that object was deleted from database and it should be removed from object manager.
     *
     * @param objectProxy is proxy object being removed
     * @param oldValue are old values associated with object
     */
    void registerDelete(P objectProxy, @Nullable V oldValue);

    /**
     * Remove given object. Used when proxy is to be released because cache has grown too big. Note that even if
     * references are removed from indices, there might still be objects that retain reference and thus might stumble
     * across invalid object proxy. Call invalidates all sets that contained given object because they are no longer
     * complete
     *
     * @param objectProxy is proxy being removed from manager
     * @param oldValue is original value associated with proxy (it is directly passed to this method, as state of proxy
     *                at this moment is undefined)
     */
    void unregister(P objectProxy, @Nullable V oldValue);

    /**
     * Should be called when database reports update but object is not kept in cache. Invalidates all sets, as it is not
     * clear to which sets updated object should belong without validating it
     */
    void registerUnknownUpdate();
}
