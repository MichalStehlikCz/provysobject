package com.provys.provysobject;

import javax.annotation.Nonnull;

@SuppressWarnings("WeakerAccess") // used as basis for subclassing in other modules
public interface ProvysNmObject extends ProvysObject {
    /**
     * @return internal name (attribute NAME_NM)
     */
    @Nonnull
    String getNameNm();
}
