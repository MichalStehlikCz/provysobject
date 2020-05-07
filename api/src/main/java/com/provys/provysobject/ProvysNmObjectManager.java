package com.provys.provysobject;

import com.provys.common.exception.RegularException;
import java.util.Optional;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Repository managing objects with internal name used as natural key.
 *
 * @param <T> is interface of managed object
 */
public interface ProvysNmObjectManager<T extends ProvysNmObject> extends ProvysObjectManager<T> {

  /**
   * Retrieve object from cache using supplied internal name. Try to load object from database if
   * not present in cache
   *
   * @param nameNm is internal name of object to be retrieved
   * @return object with specified internal name
   * @throws RegularException JAVA_MANAGER_OBJECT_NOT_FOUND_BY_NM if object with given internal name
   *                          is not found
   */
  T getByNameNm(String nameNm);

  /**
   * Retrieve object from cache using supplied internal name. Try to load object from database if
   * not present in cache. Return {@code null} if no such object exists.
   *
   * @param nameNm is internal name of object to be retrieved
   * @return object with specified internal name, {@code null} if object with such internal name
   *     doesn't exist (or is of different type)
   */
  @Nullable T getNullableByNameNm(String nameNm);

  /**
   * Retrieve object from cache using supplied internal name. Try to load object from database if
   * not present in cache
   *
   * @param nameNm is internal name of object to be retrieved
   * @return object with specified internal name, empty {@code Optional} if object with such
   *     internal name doesn't exist (or is of different type)
   */
  Optional<T> getOptionalByNameNm(String nameNm);
}
