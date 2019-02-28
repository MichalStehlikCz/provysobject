package com.provys.provysobject.impl;

import com.provys.common.exception.RegularException;
import com.provys.provysobject.ProvysNmObject;
import com.provys.provysobject.ProvysNmObjectManager;
import com.provys.provysobject.ProvysRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("WeakerAccess") // class used as basis for subclassing in other packages
public abstract class ProvysNmObjectManagerImpl<R extends ProvysRepository, O extends ProvysNmObject,
        V extends ProvysNmObjectValue, P extends ProvysNmObjectProxy<O, V>, M extends ProvysNmObjectManagerInt<O, V, P>,
        L extends ProvysNmObjectLoader<O, V, P, M>>
    extends ProvysObjectManagerImpl<R, O, V, P, M, L> implements ProvysNmObjectManagerInt<O, V, P>,
        ProvysNmObjectManager<O> {

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
        O provysObject = provysObjectByNameNm.get(Objects.requireNonNull(nameNm));
        if (provysObject != null) {
            return Optional.of(provysObject);
        }
        return getLoader().loadByNameNm(self(), nameNm);
    }

    @Override
    protected void doRegisterChange(P provysObject, @Nullable V oldValue, @Nullable V newValue) {
        super.doRegisterChange(provysObject, oldValue, newValue);
        // change of nameNm
        if ((oldValue != null) && ((newValue == null) || (!oldValue.getNameNm().equals(newValue.getNameNm())))) {
            // remove old value
            var oldObject = provysObjectByNameNm.remove(oldValue.getNameNm());
            if ((oldObject != null) && (oldObject != provysObject)) {
                // we do not want any change if old value was not registered in index
                provysObjectByNameNm.putIfAbsent(oldObject.getNameNm(), oldObject);
            }
        }
        if ((newValue != null) && ((oldValue == null) || (!newValue.getNameNm().equals(oldValue.getNameNm())))) {
            // register new value
            var oldObject = provysObjectByNameNm.put(newValue.getNameNm(), provysObject.selfAsObject());
            if ((oldObject != null) && (oldObject != provysObject)) {
                LOG.warn("Replaced {} in internal name index {}", getEntityNm(), provysObject.getNameNm());
            }
        }
    }
}