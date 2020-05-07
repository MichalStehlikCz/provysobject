package com.provys.provysobject.index;

import com.provys.common.datatype.DtUid;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Simple value class, holding Id + internal name, used for composite indices.
 */
public final class IdNameNmPair {

  private final DtUid id;
  private final String nameNm;

  /**
   * Create new id + internal name pair.
   *
   * @param id     is Id value
   * @param nameNm is internal name value
   */
  public IdNameNmPair(DtUid id, String nameNm) {
    this.id = id;
    this.nameNm = nameNm;
  }

  /**
   * Id value from pair.
   *
   * @return get Id value from pair
   */
  public DtUid getId() {
    return id;
  }

  /**
   * Internal name value from pair.
   *
   * @return internal name value from pair
   */
  public String getNameNm() {
    return nameNm;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IdNameNmPair that = (IdNameNmPair) o;
    return id.equals(that.id)
        && nameNm.equals(that.nameNm);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + nameNm.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "IdNameNmPair{"
        + "id=" + id
        + ", nameNm='" + nameNm + '\''
        + '}';
  }
}
