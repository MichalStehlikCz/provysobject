package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import com.provys.common.exception.InternalException;
import com.provys.provysobject.ProvysObject;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("WeakerAccess")
// get load runner methods must be overridden in implementation classes
public abstract class ProvysObjectLoaderImpl<O extends ProvysObject, V extends ProvysObjectValue,
    P extends ProvysObjectProxy<O, V>, M extends ProvysObjectManagerInt<O, V, P>>
    implements ProvysObjectLoader<O, V, P, M> {

  private static final Logger LOG = LogManager.getLogger(ProvysObjectLoaderImpl.class);

  protected abstract ProvysObjectLoadRunner<O, V, P, M> getLoadRunnerById(M manager, DtUid id);

  @Override
  public Optional<P> loadById(M manager, DtUid id) {
    LOG.info("Load {} by Id {}", manager::getEntityNm, () -> id);
    var result = getLoadRunnerById(manager, id).run();
    if (result.isEmpty()) {
      return Optional.empty();
    }
    if (result.size() > 1) {
      throw new InternalException(
          "Incorrect number of " + manager.getEntityNm() + " loaded by id " + id + ": " + result
              .size());
    }
    return Optional.of(result.get(0));
  }

  @Override
  public void loadValue(M manager, P objectProxy) {
    LOG.info("Load {} value for Id {}", manager::getEntityNm, objectProxy::getId);
    getLoadRunnerById(manager, objectProxy.getId()).setValueObject(objectProxy);
  }

  protected abstract ProvysObjectLoadRunner<O, V, P, M> getLoadRunnerAll(M manager);

  @Override
  public void loadAll(M manager) {
    LOG.info("Load all {}", manager::getEntityNm);
    var loadRunner = getLoadRunnerAll(manager);
    loadRunner.run();
  }

  @Override
  public String toString() {
    return "ProvysObjectLoaderImpl{}";
  }
}
