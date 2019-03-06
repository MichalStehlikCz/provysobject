package com.provys.provysobject;

import javax.annotation.Nonnull;
import java.math.BigInteger;

@SuppressWarnings("WeakerAccess") // used as basis for subclassing in other modules
public interface ProvysObject {
    /**
     * @return Id (attribute OBJECT_ID)
     */
    @Nonnull
    BigInteger getId();
}
