package com.provys.provysobject.impl;

import com.provys.common.exception.RegularException;
import com.provys.provysobject.ProvysNmObject;
import com.provys.provysobject.ProvysNmObjectManager;
import com.provys.provysobject.ProvysRepository;
import com.provys.provysobject.index.IndexUnique;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("WeakerAccess") // class used as basis for subclassing in other packages
public abstract class ProvysNmObjectManagerImpl<R extends ProvysRepository, O extends ProvysNmObject,
        V extends ProvysNmObjectValue, P extends ProvysNmObjectProxy<O, V>, M extends ProvysNmObjectManagerInt<O, V, P>,
        L extends ProvysNmObjectLoader<O, V, P, M>>
    extends ProvysObjectManagerImpl<R, O, V, P, M, L> implements ProvysNmObjectManagerInt<O, V, P>,
        ProvysNmObjectManager<O> {

    @Nonnull
    private static final Logger LOG = LogManager.getLogger(ProvysNmObjectManagerImpl.class);

    @Nonnull
    private final IndexUnique<V, P, String> provysObjectByNameNm;

    public ProvysNmObjectManagerImpl(R repository, L loader, int initialCapacity, int indexCount) {
        super(repository, loader, initialCapacity, indexCount + 1 ); // +1 for our NameNm index
        this.provysObjectByNameNm = new IndexUnique<>("NmObjectByNameNm",
                ProvysNmObjectValue::getNameNm, initialCapacity);
        addIndex(provysObjectByNameNm);
    }

    @Nonnull
    @Override
    public O getByNameNm(String nameNm) {
        return getByNameNmIfExists(nameNm).orElseThrow(() -> new RegularException(LOG,
                "JAVA_MANAGER_OBJECT_NOT_FOUND_BY_NM",
                getEntityNm() + " was not found by supplied internal name " + nameNm,
                Map.of("ENTITY_NM", getEntityNm(), "NAME_NM", nameNm)));
    }

    /**
     * Retrieve entity group from repository using supplied internal name. Try to load entity group from database if not
     * present in cache
     *
     * @param nameNm is internal name of entity group to be retrieved
     * @return entity group with specified internal name, empty optional if entity group with such internal name doesn't
     * exist
     */
    @Nonnull
    public Optional<O> getByNameNmIfExists(String nameNm) {
        var provysObject = provysObjectByNameNm.get(Objects.requireNonNull(nameNm)).
                map(ProvysObjectProxy::selfAsObject);
        if (provysObject.isEmpty()) {
            provysObject = getLoader().loadByNameNm(self(), nameNm);
        }
        return provysObject;
    }

}