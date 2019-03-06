package com.provys.provysobject.index;

import com.provys.common.exception.InternalException;
import com.provys.provysobject.impl.TestNmObjectProxyImpl;
import com.provys.provysobject.impl.TestNmObjectValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;

import static org.assertj.core.api.Assertions.*;

class IndexBaseTest {

    private static class TestIndexBase extends IndexBase<TestNmObjectValue, TestNmObjectProxyImpl> {

        private static final Logger LOG = LogManager.getLogger(TestIndexBase.class);

        TestIndexBase(String name) {
            super(name);
        }

        @Override
        public void update(TestNmObjectProxyImpl proxy, @Nullable TestNmObjectValue oldValue, @Nullable TestNmObjectValue newValue) {
            throw new InternalException(LOG, "Not implemented in  test class");
        }

        @Override
        public void delete(TestNmObjectProxyImpl proxy, TestNmObjectValue value) {
            throw new InternalException(LOG, "Not implemented in  test class");
        }

        @Override
        public void unknownUpdate() {
            throw new InternalException(LOG, "Not implemented in  test class");
        }
    }
    @Test
    void getNameTest() {
        var index = new TestIndexBase("test");
        assertThat(index.getName()).isEqualTo("test");
    }

    @Test
    void toStringTest() {
        var index = new TestIndexBase("test2");
        assertThat(index.toString()).startsWith("IndexBase{").contains("name='test2'");
    }
}