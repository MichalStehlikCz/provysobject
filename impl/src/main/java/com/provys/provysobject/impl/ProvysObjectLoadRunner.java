package com.provys.provysobject.impl;

import com.provys.common.exception.InternalException;
import com.provys.provysobject.ProvysObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("WeakerAccess") // methods are overloaded and class is used outside this library
public abstract class ProvysObjectLoadRunner<O extends ProvysObject, V extends ProvysObjectValue,
        P extends ProvysObjectProxy<O, V>, M extends ProvysObjectManagerInt<O, V, P>, S> {

    private static final Logger LOG = LogManager.getLogger(ProvysObjectLoadRunner.class);

    @Nonnull
    private final M manager;

    @SuppressWarnings("WeakerAccess")
    public ProvysObjectLoadRunner(M manager) {
        this.manager = Objects.requireNonNull(manager);
    }

    @Nonnull
    public M getManager() {
        return manager;
    }

    /**
     * Read source objects from whatever source is used in this loader to sourceObjects. Should only be called from run
     * and only be called once
     */
    @Nonnull
    protected abstract List<S> select();

    /**
     * Retrieve id of object from source object (usually JOOQ record or JsonObject)
     *
     * @param sourceObject is source object holding data
     * @return id corresponding to source object
     */
    @Nonnull
    protected abstract BigInteger getId(S sourceObject);

    @Nonnull
    protected abstract V createValueObject(S sourceObject);

    @Nonnull
    private List<P> registerAll(List<S> sourceObjects) {
        Objects.requireNonNull(sourceObjects, "Load source objects must be called before registerAll");
        List<P> result = new ArrayList<>(sourceObjects.size());
        for (var sourceObject : sourceObjects) {
            var resultObject = manager.getOrAddById(getId(sourceObject));
            resultObject.setValueObject(createValueObject(sourceObject));
            result.add(resultObject);
        }
        return result;
    }

    @Nonnull
    public List<P> run() {
        var sourceObjects = select();
        return registerAll(sourceObjects);
    }

    void setValueObject(P proxyObject) {
        var sourceObjects = select();
        if (sourceObjects.isEmpty()) {
            LOG.info(manager.getEntityNm() +  " value reload resulted in removal as it was not found using Id");
            proxyObject.deleted();
            return;
        }
        if (sourceObjects.size()>1) {
            throw new InternalException(LOG, manager.getEntityNm() +  " load value by Id resulted in multiple rows");
        }
        proxyObject.setValueObject(createValueObject(sourceObjects.get(0)));
    }
}
