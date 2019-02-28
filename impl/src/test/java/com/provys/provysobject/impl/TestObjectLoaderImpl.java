package com.provys.provysobject.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestObjectLoaderImpl extends ProvysObjectLoaderImpl<TestObject, TestObjectValue, TestObjectProxyImpl,
        TestObjectManagerImpl, TestObjectSource> implements TestObjectLoader {

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
            TestObjectManagerImpl, TestObjectSource> {

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
        protected List<TestObjectSource> select() {
            return Stream.of(new TestObjectSource(BigInteger.valueOf(1), "Test value"),
                    new TestObjectSource(BigInteger.valueOf(5), "Another test value")).
                    filter(objectSource -> (ids == null) || (ids.contains(objectSource.getId()))).
                    collect(Collectors.toList());
        }

        @Nonnull
        @Override
        protected BigInteger getId(TestObjectSource sourceObject) {
            return sourceObject.getId();
        }

        @Nonnull
        @Override
        protected TestObjectValue createValueObject(TestObjectSource sourceObject) {
            return new TestObjectValue(sourceObject.getId(), sourceObject.getValue());
        }
    }
}
