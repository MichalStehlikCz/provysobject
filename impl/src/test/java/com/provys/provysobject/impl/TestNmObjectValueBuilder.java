package com.provys.provysobject.impl;

import javax.annotation.Nullable;
import java.util.Objects;

public class TestNmObjectValueBuilder extends ProvysNmObjectValueBuilder<TestNmObjectValueBuilder, TestNmObjectValue> {


    @Nullable
    private String value;
    private boolean updValue = false;

    TestNmObjectValueBuilder() {}

    TestNmObjectValueBuilder(TestNmObjectValue value) {
        super(value);
        setValue(value.getValue().orElse(null));
    }

    TestNmObjectValueBuilder(TestNmObjectValueBuilder value) {
        super(value);
        this.value = value.value;
        this.updValue = value.updValue;
    }

    @Override
    protected TestNmObjectValueBuilder self() {
        return this;
    }

    /**
     * @return value of field value
     */
    @Nullable
    public String getValue() {
        return value;
    }

    /**
     * Set value of field value
     *
     * @param value is new value to be set
     * @return self to enable chaining
     */
    public TestNmObjectValueBuilder setValue(@Nullable String value) {
        this.value = value;
        this.updValue = true;
        return this;
    }

    /**
     * @return value of field updValue
     */
    public boolean isUpdValue() {
        return updValue;
    }

    /**
     * Set value of field updValue. If set to false, reset also field value
     *
     * @param updValue is new value to be set
     * @return self to enable chaining
     */
    public TestNmObjectValueBuilder setUpdValue(boolean updValue) {
        this.updValue = updValue;
        if (!updValue) {
            this.value = null;
        }
        return this;
    }

    /**
     * Clone builder - create new builder with exactly same set of modified properties
     *
     * @return new builder with the same properties as this. Does not copy referenced objects
     */
    @Override
    public TestNmObjectValueBuilder copy() {
        return new TestNmObjectValueBuilder(this);
    }

    @Override
    public TestNmObjectValueBuilder apply(TestNmObjectValue source) {
        if (!updValue) {
            setValue(source.getValue().orElse(null));
        }
        return super.apply(source);
    }

    /**
     * Build new value object based on values in this builder. Does validation first. Defaults are used for properties
     * that has not been set.
     *
     * @return new value object
     */
    @Override
    public TestNmObjectValue build() {
        return new TestNmObjectValue(Objects.requireNonNull(getId(), "Id is missing"),
                Objects.requireNonNull(getNameNm(), "NameNm is missing"),
                getValue());
    }
}
