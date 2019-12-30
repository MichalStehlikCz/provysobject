package com.provys.provysobject;

import com.provys.common.datatype.DtUid;

import javax.annotation.Nonnull;

@SuppressWarnings("WeakerAccess") // used as basis for subclassing in other modules
public interface ProvysObject {
    /**
     * @return Id (attribute OBJECT_ID)
     */
    @Nonnull
    DtUid getId();
}
