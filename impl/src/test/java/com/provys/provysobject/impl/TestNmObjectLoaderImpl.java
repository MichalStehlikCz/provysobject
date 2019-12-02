package com.provys.provysobject.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestNmObjectLoaderImpl extends ProvysNmObjectLoaderImpl<TestNmObject, TestNmObjectValue,
        TestNmObjectProxyImpl, TestNmObjectManagerImpl> implements TestNmObjectLoader {
    @Nonnull
    @Override
    protected LoadRunner getLoadRunnerByNameNm(TestNmObjectManagerImpl manager, String nameNm) {
        return new LoadRunner(manager, null, List.of(nameNm));
    }

    @Nonnull
    @Override
    protected LoadRunner getLoadRunnerById(TestNmObjectManagerImpl manager, BigInteger id) {
        return new LoadRunner(manager, List.of(id), null);
    }

    @Nonnull
    @Override
    protected LoadRunner getLoadRunnerAll(TestNmObjectManagerImpl manager) {
        return new LoadRunner(manager, null, null);
    }

    private static class LoadRunner extends ProvysObjectLoadRunner<TestNmObject, TestNmObjectValue,
            TestNmObjectProxyImpl, TestNmObjectManagerImpl> {

        @Nullable
        private final Set<BigInteger> ids;
        @Nullable
        private final Set<String> nameNms;

        LoadRunner(TestNmObjectManagerImpl manager, @Nullable Collection<BigInteger> ids,
                   @Nullable Collection<String> nameNms) {
            super(manager);
            if (ids != null) {
                this.ids = new HashSet<>(ids);
            } else {
                this.ids = null;
            }
            if (nameNms != null) {
                this.nameNms = new HashSet<>(nameNms);
            } else {
                this.nameNms = null;
            }
        }

        @Nonnull
        @Override
        protected List<TestNmObjectValue> select() {
            return Stream.of(new TestNmObjectSource(BigInteger.valueOf(1), "NM1", "Test value"),
                    new TestNmObjectSource(BigInteger.valueOf(5), "NM5", "Another test value"))
                    .filter(objectSource -> (ids == null) || (ids.contains(objectSource.getId())))
                    .filter(objectSource -> (nameNms == null) || (nameNms.contains(objectSource.getNameNm())))
                    .map(sourceObject -> new TestNmObjectValue(sourceObject.getId(), sourceObject.getNameNm(), sourceObject.getValue()))
                    .collect(Collectors.toList());
        }
    }
}
