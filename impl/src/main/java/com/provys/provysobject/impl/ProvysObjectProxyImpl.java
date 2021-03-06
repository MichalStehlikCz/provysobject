package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import com.provys.common.exception.InternalException;
import com.provys.provysobject.ProvysObject;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Proxy class for given PROVYS entity implements this entity's interface. Proxy object gets Id on
 * creation and retains this Id. It can only be retrieved via this entity's manager and there should
 * only be one instance of proxy for given object (id); if there are more, it might lead to various
 * consistency problems, as only the one connected to entity manager gets updated. Proxy uses
 * corresponding value class to store current values of object properties. Value class is created on
 * demand using loader and might be later removed to limit space occupied by cache; this is fully
 * transparent to users of proxy. Proxy informs its manager when values change; value class should
 * be immutable and value change is accomplished by disconnecting old value object and connecting
 * new one. Manager then should update its indices to make them consistent with new value. Proxy
 * provides access to collections of child objects and might be referenced by or contained in
 * collection of child objects of other proxy.
 *
 * @param <O> is interface corresponding to given PROVYS entity; proxy supports conversion to this
 *            interface via self()
 * @param <V> is value class corresponding to given PROVYS entity; it is used to hold property
 *            values
 * @param <M> is manager for objects corresponding to given PROVYS entity; proxies are created and
 *            managed by this manager ans should never be instantiated otherwise
 */
public abstract class ProvysObjectProxyImpl<O extends ProvysObject, V extends ProvysObjectValue,
    P extends ProvysObjectProxy<O, V>, M extends ProvysObjectManagerInt<O, V, P>>
    implements ProvysObjectProxy<O, V> {

  private final M manager;
  private final DtUid id;
  private volatile long lastUsed = 0;
  private @Nullable V valueObject;
  private boolean deleted = false;

  public ProvysObjectProxyImpl(M manager, DtUid id) {
    this.manager = Objects.requireNonNull(manager);
    this.id = Objects.requireNonNull(id);
  }

  protected M getManager() {
    return manager;
  }

  /**
   * Reference to self as correct proxy subclass P.
   *
   * @return reference to self as correct proxy subclass P
   */
  protected abstract P self();

  @Override
  public long getLastUsed() {
    return lastUsed;
  }

  @Override
  public void setLastUsed() {
    lastUsed = System.currentTimeMillis();
  }

  /**
   * Set value object in this proxy. Method should only be invoked by appropriate loader or updater
   * (but must be published as loader is often implemented in different package)
   *
   * @param valueObject is value object to be assigned to proxy
   */
  @Override
  @SuppressWarnings("squid:S1192")
  // it is easier to have error message on each place and not construct them from constants
  public synchronized void setValueObject(V valueObject) {
    if (deleted) {
      throw new InternalException(
          "Cannot set value of deleted " + getManager().getEntityNm() + " proxy");
    }
    if (!Objects.equals(this.valueObject, valueObject)) {
      var oldValue = this.valueObject;
      this.valueObject = valueObject;
      getManager().registerUpdate(self(), oldValue, valueObject);
    }
    setLastUsed();
  }

  @Override
  public synchronized void discardValueObject() {
    if (deleted) {
      throw new InternalException(
          "Cannot discard value of deleted " + getManager().getEntityNm() + " proxy");
    }
    if (this.valueObject != null) {
      getManager().registerUpdate(self(), this.valueObject, null);
      this.valueObject = null;
    }
  }

  @Override
  public synchronized void deleted() {
    getManager().registerDelete(self(), this.valueObject);
    this.valueObject = null;
    this.deleted = true;
  }

  public synchronized boolean isDeleted() {
    return deleted;
  }

  protected synchronized @Nullable V getValueObject() {
    if (deleted) {
      throw new InternalException(
          "Cannot get value of deleted " + getManager().getEntityNm() + " proxy");
    }
    setLastUsed();
    return valueObject;
  }

  /**
   * Ensure that value object is loaded and valid (not expired).
   *
   * @return valid value object
   */
  public V validateValueObject() {
    // if somebody discards valueObject, we want to retrieve older value rather than null
    var result = valueObject;
    if (result == null) {
      if (deleted) {
        throw new InternalException(
            "Cannot validate value of deleted " + getManager().getEntityNm() + " proxy");
      }
      synchronized (this) {
        if (valueObject == null) {
          // we might get it via synchronisation... */
          getManager().loadValueObject(self());
          if (valueObject == null) {
            throw new InternalException("Load " + getManager().getEntityNm()
                + " failed - value is empty");
          }
        }
        result = valueObject;
      }
    }
    setLastUsed();
    return result;
  }

  @Override
  public DtUid getId() {
    if (deleted) {
      throw new InternalException(
          "Cannot get Id of deleted " + getManager().getEntityNm() + " proxy");
    }
    return id;
  }

  /**
   * If two instances have same Id, they represent same object and thus are considered to be the
   * same.
   *
   * @param other is other object this is being compared to
   * @return true if this and other are proxies of the same type with same Id, false otherwise
   */
  @Override
  public boolean equals(@Nullable Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    var otherProxy = (ProvysObjectProxyImpl<?, ?, ?, ?>) other;
    return getId().equals(otherProxy.getId());
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "ProvysObjectProxyImpl{"
        + "manager=" + manager
        + ", id=" + id
        + ", lastUsed=" + lastUsed
        + ", valueObject=" + valueObject
        + ", deleted=" + deleted
        + '}';
  }
}
