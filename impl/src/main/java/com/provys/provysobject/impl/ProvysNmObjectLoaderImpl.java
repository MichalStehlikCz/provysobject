package com.provys.provysobject.impl;

import com.provys.common.exception.InternalException;
import com.provys.provysobject.ProvysNmObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Optional;

public abstract class ProvysNmObjectLoaderImpl<O extends ProvysNmObject, V extends ProvysNmObjectValue,
        P extends ProvysNmObjectProxy<O, V>, M extends ProvysNmObjectManagerInt<O, V, P>, S>
        extends ProvysObjectLoaderImpl<O, V, P, M, S>
        implements ProvysNmObjectLoader<O, V, P, M> {

    @Nonnull
    private static final Logger LOG = LogManager.getLogger(ProvysNmObjectLoaderImpl.class);

    @SuppressWarnings("WeakerAccess") // method needs to be implemented in subclasses
    @Nonnull
    protected abstract ProvysObjectLoadRunner<O, V, P, M, S> getLoadRunnerByNameNm(M manager, String nameNm);

    @Nonnull
    @Override
    public Optional<O> loadByNameNm(M manager, String nameNm) {
        LOG.info("Load {} by nameNm {}", manager::getEntityNm, nameNm::toString);
        var result = getLoadRunnerByNameNm(manager, nameNm).run();
        if (result.isEmpty()) {
            return Optional.empty();
        }
        if (result.size() > 1) {
            throw new InternalException(LOG,
                    "Incorrect number of " + manager.getEntityNm() + " loaded by internal name " + nameNm + ": "
                            + result.size());
        }
        return Optional.of(result.get(0).selfAsObject());
    }
}
