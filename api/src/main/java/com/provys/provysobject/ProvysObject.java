package com.provys.provysobject;

import com.provys.common.datatype.DtUid;

/**
 * Interface provided by all provys objects.
 */
public interface ProvysObject {

  /**
   * Object Id (attribute OBJECT_ID).
   *
   * @return Id (attribute OBJECT_ID)
   */
  DtUid getId();
}
