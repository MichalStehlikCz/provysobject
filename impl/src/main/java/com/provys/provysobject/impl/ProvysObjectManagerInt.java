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
     * @param deleted indicates if underlying object has ben deleted from database; used to determine if indices
     *               marked as fully cached containing this value should still be considered valid after value removal,
     *                or if such indices should be marked as partial because lifted value might still be valid in
     *                database and will now be missing from index
     */
    void registerUpdate(P objectProxy, @Nullable V oldValue, @Nullable V newValue, boolean deleted);

    /**
     * Remove given object. Used as reaction to delete or when proxy is to be released because cache has grown too big.
     * Note that even if references are removed from indices, there might still be objects that retain reference and
     * thus might stumble across invalid object proxy
     *
     * @param objectProxy is proxy being removed from manager
     * @param oldValue is original value associated with proxy (it is directly passed to this method, as state of proxy
     *                at this moment is undefined)
     * @param deleted indicates if underlying object has ben deleted from database
     */
    void unregister(P objectProxy, @Nullable V oldValue, boolean deleted);
}
