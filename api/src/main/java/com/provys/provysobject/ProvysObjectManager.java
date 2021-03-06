package com.provys.provysobject;

import com.provys.common.datatype.DtUid;
import com.provys.common.exception.RegularException;
import java.util.Collection;
import java.util.Optional;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Ancestor of object repository.
 *
 * @param <T> is type of managed object
 */
public interface ProvysObjectManager<T extends ProvysObject> {

  /**
   * Retrieve internal name of PROVYS entity, managed by this manager.
   *
   * @return internal name of PROVYS entity
   */
  String getEntityNm();

  /**
   * Retrieve object from cache using supplied UID. Try to load object from database if not present
   * in cache
   *
   * @param id is id of object to be retrieved
   * @return object with specified id
   * @throws RegularException JAVA_MANAGER_OBJECT_NOT_FOUND if object with given id is not found
   */
  T getById(DtUid id);

  /**
   * Retrieve object from cache using supplied UID. Try to load object from database if not present
   * in cache
   *
   * @param id is id of object to be retrieved
   * @return object with specified id, {@code null} if object doesn't exist or is of different type
   */
  @Nullable T getNullableById(DtUid id);

  /**
   * Retrieve object from cache using supplied UID. Try to load object from database if not present
   * in cache
   *
   * @param id is id of object to be retrieved
   * @return object with specified id, empty {@code Optional} if object doesn't exist or is of
   *     different type
   */
  Optional<T> getOptionalById(DtUid id);

  /**
   * Retrieve all entity groups. Load all entity groups from database to cache
   *
   * @return collection of all entity groups in database
   */
  Collection<T> getAll();
}
