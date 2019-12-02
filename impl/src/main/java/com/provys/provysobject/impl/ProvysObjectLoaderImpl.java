package com.provys.provysobject.impl;

import com.provys.common.exception.InternalException;
import com.provys.provysobject.ProvysObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.util.Optional;

@SuppressWarnings("WeakerAccess") // get load runner methods must be overridden in implementation classes
public abstract class ProvysObjectLoaderImpl<O extends ProvysObject, V extends ProvysObjectValue,
        P extends ProvysObjectProxy<O, V>, M extends ProvysObjectManagerInt<O, V, P>>
        implements ProvysObjectLoader<O, V, P, M> {

    @Nonnull
    private static final Logger LOG = LogManager.getLogger(ProvysObjectLoaderImpl.class);

    @Nonnull
    protected abstract ProvysObjectLoadRunner<O, V, P, M> getLoadRunnerById(M manager, BigInteger id);

    @Nonnull
    @Override
    public Optional<P> loadById(M manager, BigInteger id) {
        LOG.info("Load {} by Id {}", manager::getEntityNm, () -> id);
        var result = getLoadRunnerById(manager, id).run();
        if (result.isEmpty()) {
            return Optional.empty();
        }
        if (result.size() > 1) {
            throw new InternalException(LOG,
                    "Incorrect number of " + manager.getEntityNm() + " loaded by id " + id + ": " + result.size());
        }
        return Optional.of(result.get(0));
    }

    @Override
    public void loadValue(M manager, P objectProxy) {
        LOG.info("Load {} value for Id {}", manager::getEntityNm, objectProxy::getId);
        getLoadRunnerById(manager, objectProxy.getId()).setValueObject(objectProxy);
    }

    @Nonnull
    protected abstract ProvysObjectLoadRunner<O, V, P, M> getLoadRunnerAll(M manager);

    @Override
    public void loadAll(M manager) {
        LOG.info("Load all {}", manager::getEntityNm);
        var loadRunner = getLoadRunnerAll(manager);
        loadRunner.run();
    }
}
