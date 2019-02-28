package com.provys.provysobject;

import javax.annotation.Nonnull;
import java.util.Optional;
import com.provys.common.exception.RegularException;

public interface ProvysNmObjectManager<T extends ProvysNmObject> extends ProvysObjectManager<T> {

    /**
     * Retrieve object from cache using supplied internal name. Try to load object from database if not present in cache
     *
     * @param nameNm is internal name of object to be retrieved
     * @return object with specified internal name
     * @throws RegularException JAVA_MANAGER_OBJECT_NOT_FOUND_BY_NM if object with given internal name is not found
     */
    @Nonnull
    T getByNameNm(String nameNm);

    /**
     * Retrieve object from cache using supplied internal name. Try to load object from database if not present in cache
     *
     * @param nameNm is internal name of object to be retrieved
     * @return object with specified internal name, empty {@code Optional} if object with such internal name doesn't
     * exist (or is of different type)
     */
    @Nonnull
    Optional<T> getByNameNmIfExists(String nameNm);
}
