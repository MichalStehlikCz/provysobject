package com.provys.provysobject.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestObjectLoaderImpl extends ProvysObjectLoaderImpl<TestObject, TestObjectValue, TestObjectProxyImpl,
        TestObjectManagerImpl> implements TestObjectLoader {

    @Nonnull
    @Override
    protected LoadRunner getLoadRunnerById(TestObjectManagerImpl manager, BigInteger id) {
        return new LoadRunner(manager, List.of(id));
    }

    @Nonnull
    @Override
    protected LoadRunner getLoadRunnerAll(TestObjectManagerImpl manager) {
        return new LoadRunner(manager);
    }

    private static class LoadRunner extends ProvysObjectLoadRunner<TestObject, TestObjectValue, TestObjectProxyImpl,
            TestObjectManagerImpl> {

        @Nullable
        private final Set<BigInteger> ids;

        LoadRunner(TestObjectManagerImpl manager, Collection<BigInteger> ids) {
            super(manager);
            this.ids = new HashSet<>(ids);
        }

        LoadRunner(TestObjectManagerImpl manager) {
            super(manager);
            this.ids = null;
        }

        @Nonnull
        @Override
        protected List<TestObjectValue> select() {
            return Stream.of(new TestObjectSource(BigInteger.valueOf(1), "Test value"),
                    new TestObjectSource(BigInteger.valueOf(5), "Another test value"),
                    new TestObjectSource(BigInteger.valueOf(6), "Duplicate test value"),
                    new TestObjectSource(BigInteger.valueOf(6), "Duplicate test value - row 2"))
                    .filter(objectSource -> (ids == null) || (ids.contains(objectSource.getId())))
                    .map(sourceObject -> new TestObjectValue(sourceObject.getId(), sourceObject.getValue()))
                    .collect(Collectors.toList());
        }
    }
}
