package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import com.provys.common.exception.InternalException;
import com.provys.common.exception.RegularException;
import com.provys.provysobject.ProvysObject;
import com.provys.provysobject.ProvysRepository;
import com.provys.provysobject.index.Index;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.initialization.qual.UnderInitialization;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Gives access to objects of given type, holds objects and ensures there is only one instance for
 * each Id. Keeps indices to find cached objects using other columns than primary key. It uses
 * loader to load object to cache using supplied query (id, name, load all objects...); loader
 * should be supplied on creation and is retained throughout operation. It should receive
 * notification whenever data change and to update its indices; updates are generally received from
 * associated proxy object or loader, manager expects updates or deletions are registered using
 * register and unregister calls but does not make any assumptions about mechanism for receiving
 * these changes. It is up to manager to decide if data should be kept in index or index entries are
 * released and loader is used to retrieve appropriate objects.
 *
 * @param <R> is repository containing given manager. Repository enables crossing entity boundaries
 *            - objects within same repository can reference each other and references are resolved
 *            via repository during load time. It is common for all object managers to keep
 *            reference to repository and thus is included here, even though no other functionality
 *            is required of repository on this level
 * @param <V> is value object representing given entity; object manager gets old and new values on
 *            object updates to maintain indices
 * @param <P> proxy type, implementing incremental load for given object interface
 * @param <L> is loader used to verify object existence and load values to objects in repository
 */
public abstract class ProvysObjectManagerImpl<R extends ProvysRepository, O extends ProvysObject,
    V extends ProvysObjectValue, P extends ProvysObjectProxy<O, V>,
    M extends ProvysObjectManagerInt<O, V, P>, L extends ProvysObjectLoader<O, V, P, M>>
    implements ProvysObjectManagerInt<O, V, P> {

  private static final Logger LOG = LogManager.getLogger(ProvysObjectManagerImpl.class);

  private final R repository;
  private final L loader;

  private final Map<DtUid, P> provysObjectById;
  private final List<Index<V, P>> indices;

  /**
   * Create new object manager.
   *
   * @param repository      is repository manager belongs to
   * @param loader          is loader used to load new objects
   * @param initialCapacity is capacity (number of object) to be allocated after creation
   * @param indices         is number of indices
   */
  public ProvysObjectManagerImpl(R repository, L loader, int initialCapacity, int indices) {
    this.repository = repository;
    this.loader = loader;
    this.provysObjectById = new ConcurrentHashMap<>(initialCapacity);
    this.indices = new ArrayList<>(indices);
  }

  protected abstract M self();

  /**
   * Retrieve index with given name. If there are multiple indices with the same name, retrieves the
   * first one
   *
   * @param name is name of index
   * @return index with given name
   */
  protected Index<V, P> getIndex(String name) {
    return indices.stream()
        .filter(index -> index.getName().equals(name))
        .findFirst()
        .orElseThrow(
            () -> new InternalException("Index " + name + " not found in manager " + this));
  }

  /**
   * Add index to collection of maintained indices. Checks that manager is empty, should only be
   * used from constructor.
   *
   * @param index is index to be added
   */
  protected final void addIndex(Index<V, P> index) {
    if (!provysObjectById.isEmpty()) {
      throw new InternalException("Cannot add index " + index + " to non-empty repository");
    }
    indices.add(index);
  }

  /**
   * Repository this manager belongs to.
   *
   * @return repository this manager belongs to
   */
  public R getRepository() {
    return repository;
  }

  /**
   * Loader used by this manager to retrieve objects and their values.
   *
   * @return loader used by this manager to retrieve objects and their values
   */
  public L getLoader() {
    return loader;
  }

  @Override
  public O getById(DtUid id) {
    return getOptionalById(id)
        .orElseThrow(() -> new RegularException("JAVA_MANAGER_OBJECT_NOT_FOUND",
            getEntityNm() + " not found by id: " + id,
            Map.of("ENTITY_NM", getEntityNm(), "ID", id.toString())));
  }

  @Override
  public Optional<O> getOptionalById(DtUid id) {
    P provysObject = provysObjectById.get(id);
    if (provysObject != null) {
      // object found in cache
      return Optional.of(provysObject.selfAsObject());
    }
    // object has to be loaded
    return getLoader()
        .loadById(self(), id)
        .map(ProvysObjectProxy::selfAsObject);
  }

  @Override
  public @Nullable O getNullableById(DtUid id) {
    return getOptionalById(id).orElse(null);
  }

  @Override
  public Collection<O> getAll() {
    getLoader().loadAll(self());
    return provysObjectById.values().stream()
        .map(ProvysObjectProxy::selfAsObject)
        .collect(Collectors.toList());
  }

  /**
   * Method creates new proxy for given Id. Used by loader - loader itself only loads values to
   * proxy, but does not have access to proxy constructor. Proxy is only created if it doesn't exist
   * in managers repository
   *
   * @param id is Id of object to be proxied
   * @return proxy for object with given Id
   */
  protected abstract P getNewProxy(DtUid id);

  /**
   * Retrieve entity group if already loaded to cache, otherwise create new proxy for given id.
   * Should only be called internally as method does not verify existence of given object in
   * database.
   *
   * @param id is Id of entity group being looked for
   * @return entity group present in cache or newly added proxy
   */
  @Override
  public P getOrAddById(DtUid id) {
    return provysObjectById.computeIfAbsent(id, this::getNewProxy);
  }

  /**
   * Use loader to load value to proxy.
   *
   * @param objectProxy is proxy that should have value loaded / refreshed by loader
   */
  @Override
  public void loadValueObject(P objectProxy) {
    getLoader().loadValue(self(), objectProxy);
  }

  /**
   * Register given object in indices. Verifies that object proxy has been previously registered for
   * its id, if not, throws exception, otherwise defers change registration to doRegisterChange
   * method
   *
   * @param objectProxy is proxy being updated
   * @param oldValue    is old value, null indicates that value has been loaded to new or
   *                    disconnected proxy
   * @param newValue    is new value being set to proxy; might be null if proxy is being relieved
   *                    from cache, even though object is not deleted from database
   */
  @Override
  public void registerUpdate(P objectProxy, @Nullable V oldValue, @Nullable V newValue) {
    //noinspection ObjectEquality
    if (provysObjectById.get(objectProxy.getId()) == objectProxy) {
      for (var index : indices) {
        index.update(objectProxy, oldValue, newValue);
      }
    } else {
      throw new InternalException(
          "Register change " + getEntityNm() + " called on unregistered object proxy");
    }
  }

  /**
   * Remove given object. Used as reaction to delete. Note that even if references are removed from
   * indices, there might still be objects that retain reference and thus might stumble across
   * invalid object proxy
   *
   * @param objectProxy is proxy whose underlying object has been deleted
   * @param oldValue    is value that was retained in proxy
   */
  @Override
  public void registerDelete(P objectProxy, @Nullable V oldValue) {
    // remove from primary index
    if (provysObjectById.remove(objectProxy.getId(), objectProxy)) {
      // if it was registered, remove from all other indices
      if (oldValue != null) {
        for (var index : indices) {
          index.delete(objectProxy, oldValue);
        }
      }
    } else {
      // just log warning, no point trying to remove value
      LOG.warn("Call to delete from {} manager invoked on not registered object {}", getEntityNm(),
          objectProxy);
    }
  }

  @Override
  public void unregister(P objectProxy, @Nullable V oldValue) {
    // remove from primary index
    if (provysObjectById.remove(objectProxy.getId(), objectProxy)) {
      // if it was registered, remove from all other indices
      if (oldValue != null) {
        for (var index : indices) {
          // we register it as update from value to null, as it invalidates sets object was
          // contained in
          index.update(objectProxy, oldValue, null);
        }
      }
    } else {
      // just log warning, no point trying to remove value
      LOG.warn("Call to delete from {} manager invoked on not registered object {}", getEntityNm(),
          objectProxy);
    }
  }

  @Override
  public void registerUnknownUpdate() {
    for (var index : indices) {
      index.unknownUpdate();
    }
  }

  @Override
  public String toString() {
    return "ProvysObjectManagerImpl{"
        + "repository=" + repository
        + ", loader=" + loader
        + ", indices=" + indices
        + ", provysObjectById=" + provysObjectById
        + '}';
  }
}
