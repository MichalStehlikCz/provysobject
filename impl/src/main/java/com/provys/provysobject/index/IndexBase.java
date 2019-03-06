package com.provys.provysobject.index;

import com.provys.provysobject.impl.ProvysObjectProxy;
import com.provys.provysobject.impl.ProvysObjectValue;

import javax.annotation.Nonnull;
import java.util.Objects;

abstract class IndexBase<V extends ProvysObjectValue, P extends ProvysObjectProxy<?, V>> implements Index<V, P> {

    @Nonnull
    private final String name;

    IndexBase(String name) {
        this.name = Objects.requireNonNull(name);
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "IndexBase{" +
                "name='" + name + '\'' +
                '}';
    }
}
