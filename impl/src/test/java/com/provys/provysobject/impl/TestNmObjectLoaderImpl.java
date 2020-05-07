package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TestNmObjectLoaderImpl extends
    ProvysNmObjectLoaderImpl<TestNmObject, TestNmObjectValue,
        TestNmObjectProxyImpl, TestNmObjectManagerImpl> implements TestNmObjectLoader {

  @Override
  protected LoadRunner getLoadRunnerByNameNm(TestNmObjectManagerImpl manager, String nameNm) {
    return new LoadRunner(manager, null, List.of(nameNm));
  }

  @Override
  protected LoadRunner getLoadRunnerById(TestNmObjectManagerImpl manager, DtUid id) {
    return new LoadRunner(manager, List.of(id), null);
  }

  @Override
  protected LoadRunner getLoadRunnerAll(TestNmObjectManagerImpl manager) {
    return new LoadRunner(manager, null, null);
  }

  private static class LoadRunner extends ProvysObjectLoadRunner<TestNmObject, TestNmObjectValue,
      TestNmObjectProxyImpl, TestNmObjectManagerImpl> {

    private final @Nullable Set<DtUid> ids;
    private final @Nullable Set<String> nameNms;

    LoadRunner(TestNmObjectManagerImpl manager, @Nullable Collection<DtUid> ids,
        @Nullable Collection<String> nameNms) {
      super(manager);
      this.ids = (ids != null) ? new HashSet<>(ids) : null;
      this.nameNms = (nameNms != null) ? new HashSet<>(nameNms) : null;
    }

    @Override
    protected List<TestNmObjectValue> select() {
      return Stream.of(new TestNmObjectSource(DtUid.valueOf("1"), "NM1", "Test value"),
          new TestNmObjectSource(DtUid.valueOf("5"), "NM5", "Another test value"))
          .filter(objectSource -> (ids == null) || ids.contains(objectSource.getId()))
          .filter(objectSource -> (nameNms == null) || nameNms.contains(objectSource.getNameNm()))
          .map(sourceObject -> new TestNmObjectValue(sourceObject.getId(), sourceObject.getNameNm(),
              sourceObject.getValue()))
          .collect(Collectors.toList());
    }
  }
}
