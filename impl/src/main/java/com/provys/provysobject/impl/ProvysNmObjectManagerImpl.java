package com.provys.provysobject.impl;

import com.provys.common.exception.RegularException;
import com.provys.provysobject.ProvysNmObject;
import com.provys.provysobject.ProvysNmObjectManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

abstract public class ProvysNmObjectManagerImpl<R extends ProvysRepositoryImpl, V extends ProvysNmObjectValue,
        O extends ProvysNmObject, M extends ProvysNmObjectManagerImpl<R, V, O, ?, ?>,
        L extends ProvysNmObjectLoader<V, O, M, ? extends ProvysNmObjectProxy<V, O, M>>>
    extends ProvysObjectManagerImpl<R, V, O, M, L>
        implements ProvysNmObjectManager<O> {

    @Nonnull
    private static final Logger LOG = LogManager.getLogger(ProvysNmObjectManagerImpl.class);

    @Nonnull
    private final Map<String, O> provysObjectByNameNm;

    public ProvysNmObjectManagerImpl(R repository, L loader, int initialCapacity) {
        super(repository, loader, initialCapacity);
        this.provysObjectByNameNm = new ConcurrentHashMap<>(initialCapacity);
    }

    @Nonnull
    @Override
    public O getByNameNm(String nameNm) {
        O provysObject = provysObjectByNameNm.get(Objects.requireNonNull(nameNm));
        if (provysObject == null) {
            provysObject = getLoader().loadByNameNm(self(), nameNm).
                    orElseThrow(() -> new RegularException(LOG, "MANAGER_OBJECTNOTFOUNDBYNM",
                            getEntityNm() + " was not found by supplied internal name " + nameNm,
                            Map.of("ENTITY_NM", getEntityNm(), "NAME_NM", nameNm)));
        }
        return provysObject;
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
        O provysObject = provysObjectByNameNm.get(Objects.requireNonNull(nameNm));
        if (provysObject == null) {
            provysObject = getLoader().loadByNameNm(self(), nameNm).orElse(null);
        }
        return Optional.ofNullable(provysObject);
    }
}
