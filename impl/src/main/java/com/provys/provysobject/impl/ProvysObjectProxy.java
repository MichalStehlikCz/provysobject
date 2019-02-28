package com.provys.provysobject.impl;

import com.provys.provysobject.ProvysObject;

import javax.annotation.Nonnull;

interface ProvysObjectProxy<O extends ProvysObject, V extends ProvysObjectValue> extends ProvysObject {

    /**
     * @return reference to itself, retyped to indicate it fulfills contract of object subclass O
     */
    @Nonnull
    O selfAsObject();

    /**
     * Set value object in this proxy. Method should only be invoked by appropriate loader or updater (but must be
     * published as loader is often implemented in different package)
     *
     * @param valueObject is value object to be assigned to proxy
     */
    void setValueObject(V valueObject);

    /**
     * Mark proxy as invalid - method used when object is removed from database.
     * Proxy disconnects itself from object manager, releases value object and will throw an exception if accessed
     */
    void deleted();
}
