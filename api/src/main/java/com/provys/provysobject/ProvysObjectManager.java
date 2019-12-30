package com.provys.provysobject;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

import com.provys.common.datatype.DtUid;
import com.provys.common.exception.RegularException;

@SuppressWarnings("WeakerAccess") // used as basis for subclassing in other modules
public interface ProvysObjectManager<T extends ProvysObject> {
    /**
     * Retrieve internal name of PROVYS entity, managed by this manager
     */
    @Nonnull
    String getEntityNm();

    /**
     * Retrieve object from cache using supplied UID. Try to load object from database if not present in cache
     *
     * @param id is id of object to be retrieved
     * @return object with specified id
     * @throws RegularException JAVA_MANAGER_OBJECT_NOT_FOUND if object with given id is not found
     */
    @Nonnull
    T getById(DtUid id);

    /**
     * Retrieve object from cache using supplied UID. Try to load object from database if not present in cache
     *
     * @param id is id of object to be retrieved
     * @return object with specified id, empty {@code Optional} if object doesn't exist or is of different type
     */
    @Nonnull
    Optional<T> getByIdIfExists(DtUid id);

    /**
     * Retrieve all entity groups. Load all entity groups from database to cache
     *
     * @return collection of all entity groups in database
     */
    @Nonnull
    Collection<T> getAll();
}
