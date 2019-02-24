package com.provys.provysobject;

import javax.annotation.Nonnull;
import java.math.BigInteger;

public interface ProvysObject {
    /**
     * @return Id (attribute OBJECT_ID)
     */
    @Nonnull
    BigInteger getId();
}
