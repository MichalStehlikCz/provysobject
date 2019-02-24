package com.provys.provysobject.impl;

import com.provys.common.exception.InternalException;
import com.provys.provysobject.ProvysObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Proxy class for given PROVYS entity implements this entity's interface. Proxy object gets Id on creation and retains
 * this Id. It can only be retrieved via this entity's manager and there should only be one instance of proxy for given
 * object (id); if there are more, it might lead to various consistency problems, as only the one connected to entity
 * manager gets updated.
 * Proxy uses corresponding value class to store current values of object properties. Value class is created on demand
 * using loader and might be later removed to limit space occupied by cache; this is fully transparent to users of
 * proxy.
 * Proxy informs its manager when values change; value class should be immutable and value change is accomplished by
 * removing old value object and connecting new one. Manager then should update its indices to make them consistent with
 * new values.
 * Proxy provides access to collections of child objects and might be contained in collection of child objects of other
 * proxy.
 *
 * @param <V> is value class corresponding to given PROVYS entity
 */
abstract public class ProvysObjectProxy<V extends ProvysObjectValue, O extends ProvysObject,
        M extends ProvysObjectManagerImpl<?, V, O, ?, ?>>
        implements ProvysObject {

    @Nonnull
    private static final Logger LOG = LogManager.getLogger(ProvysObjectProxy.class);

    @Nonnull
    private final M manager;
    @Nonnull
    private final BigInteger id;
    @Nullable
    private V value;

    ProvysObjectProxy(M manager, BigInteger id) {
        this.manager = Objects.requireNonNull(manager);
        this.id = Objects.requireNonNull(id);
    }

    @Nonnull
    protected M getManager() {
        return manager;
    }

    @Nonnull
    abstract protected O self();

    public void setValue(V value) {
        var oldValue = this.value;
        this.value = value;
        getManager().registerChange(self(), oldValue, value);
    }

    abstract protected void loadValue();

    @Nonnull
    protected V getValue() {
        if (value == null) {
            loadValue();
            if (value == null) {
                throw new InternalException(LOG, "Load entity grp failed - value empty");
            }
        }
        return value;
    }

    @Nonnull
    public BigInteger getId() {
        return id;
    }

    /**
     * If two instances have same Id, they represent same object and thus are considered to be the same
     *
     * @param other is other object this is being compared to
     * @return true if this and other are proxies of the same type with same Id, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        ProvysObjectProxy otherProxy = (ProvysObjectProxy) other;
        return getId().equals(otherProxy.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Nonnull
    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "id=" + id +
                ", value=" + (value == null ? "null" : value.toString()) +
                '}';
    }
}
