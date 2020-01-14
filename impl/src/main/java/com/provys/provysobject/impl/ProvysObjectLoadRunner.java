package com.provys.provysobject.impl;

import com.provys.common.exception.InternalException;
import com.provys.provysobject.ProvysObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class is basis for classes, implementing retrieval of data from database or remote service
 *
 * @param <O> is interface, representing objects loaded by this class
 * @param <V> is value object corresponding to interface O
 * @param <P> is proxy class corresponding to interface O
 * @param <M> is manager holding cache of objects of type O
 */
@SuppressWarnings("WeakerAccess") // methods are overloaded and class is used outside this library
public abstract class ProvysObjectLoadRunner<O extends ProvysObject, V extends ProvysObjectValue,
        P extends ProvysObjectProxy<O, V>, M extends ProvysObjectManagerInt<O, V, P>> {

    private static final Logger LOG = LogManager.getLogger(ProvysObjectLoadRunner.class);

    @Nonnull
    private final M manager;

    /**
     * Create load runner for given manager. Registers manager; note that children should also register condition that
     * will be used in select statement - but this ancestor doesn't know what type of condition will be used (sql
     * condition, parameters for web service call, ...)
     *
     * @param manager is manager this loader serves
     */
    @SuppressWarnings("WeakerAccess")
    public ProvysObjectLoadRunner(M manager) {
        this.manager = Objects.requireNonNull(manager);
    }

    /**
     * @return manager this load runner serves
     */
    @Nonnull
    public M getManager() {
        return manager;
    }

    /**
     * Read source objects from whatever source is used in this loader to sourceObjects. Should only be called from run
     * and only be called once
     */
    @Nonnull
    protected abstract List<V> select();

    @Nonnull
    private List<P> registerAll(List<V> sourceObjects) {
        List<P> result = new ArrayList<>(sourceObjects.size());
        for (var sourceObject : sourceObjects) {
            var resultObject = manager.getOrAddById(sourceObject.getId());
            resultObject.setValueObject(sourceObject);
            result.add(resultObject);
        }
        return result;
    }

    /**
     * Retrieve all objects from source and register resulting objects
     *
     * @return list of proxies corresponding to retrieved objects
     */
    @Nonnull
    public List<P> run() {
        var sourceObjects = select();
        return registerAll(sourceObjects);
    }

    void setValueObject(P proxyObject) {
        var sourceObjects = select();
        if (sourceObjects.isEmpty()) {
            LOG.info("{} value reload resulted in removal as it was not found using Id", manager::getEntityNm);
            proxyObject.deleted();
            return;
        }
        if (sourceObjects.size()>1) {
            throw new InternalException(manager.getEntityNm() +  " load value by Id resulted in multiple rows");
        }
        proxyObject.setValueObject(sourceObjects.get(0));
    }
}
