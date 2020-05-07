package com.provys.provysobject;

/**
 * Common ancestor for objects using internal name as natural key.
 */
public interface ProvysNmObject extends ProvysObject {

  /**
   * Internal name (attribute NAME_NM).
   *
   * @return internal name (attribute NAME_NM)
   */
  String getNameNm();
}
