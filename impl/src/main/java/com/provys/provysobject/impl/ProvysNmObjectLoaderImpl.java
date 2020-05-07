package com.provys.provysobject.impl;

import com.provys.common.exception.InternalException;
import com.provys.provysobject.ProvysNmObject;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Ancestor for loader of objects identified by internal name.
 *
 * @param <O> is object type (interface) this loader works for
 * @param <V> is value type corresponding to object type O
 * @param <P> is proxy type corresponding to object type O
 * @param <M> is manager caching objects of type O
 */
public abstract class ProvysNmObjectLoaderImpl<O extends ProvysNmObject,
    V extends ProvysNmObjectValue, P extends ProvysNmObjectProxy<O, V>,
    M extends ProvysNmObjectManagerInt<O, V, P>>
    extends ProvysObjectLoaderImpl<O, V, P, M>
    implements ProvysNmObjectLoader<O, V, P, M> {

  private static final Logger LOG = LogManager.getLogger(ProvysNmObjectLoaderImpl.class);

  protected abstract ProvysObjectLoadRunner<O, V, P, M> getLoadRunnerByNameNm(M manager,
      String nameNm);

  @Override
  public Optional<O> loadByNameNm(M manager, String nameNm) {
    LOG.info("Load {} by nameNm {}", manager::getEntityNm, nameNm::toString);
    var result = getLoadRunnerByNameNm(manager, nameNm).run();
    if (result.isEmpty()) {
      return Optional.empty();
    }
    if (result.size() > 1) {
      throw new InternalException(
          "Incorrect number of " + manager.getEntityNm() + " loaded by internal name " + nameNm
              + ": "
              + result.size());
    }
    return Optional.of(result.get(0).selfAsObject());
  }
}
