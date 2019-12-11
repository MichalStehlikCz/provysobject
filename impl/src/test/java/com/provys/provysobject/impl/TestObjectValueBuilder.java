package com.provys.provysobject.impl;

import javax.annotation.Nullable;
import java.util.Objects;

class TestObjectValueBuilder extends ProvysObjectValueBuilder<TestObjectValueBuilder, TestObjectValue> {

    @Nullable
    private String value;
    private boolean updValue = false;

    TestObjectValueBuilder() {}

    TestObjectValueBuilder(TestObjectValue value) {
        super(value);
        setValue(value.getValue());
    }

    TestObjectValueBuilder(TestObjectValueBuilder value) {
        super(value);
        this.value = value.value;
        this.updValue = value.updValue;
    }

    @Override
    protected TestObjectValueBuilder self() {
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
    public TestObjectValueBuilder setValue(String value) {
        this.value = Objects.requireNonNull(value, "Value cannot be set to null");
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
    public TestObjectValueBuilder setUpdValue(boolean updValue) {
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
    public TestObjectValueBuilder copy() {
        return new TestObjectValueBuilder(this);
    }

    @Override
    public TestObjectValueBuilder apply(TestObjectValue source) {
        if (!updValue) {
            setValue(source.getValue());
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
    public TestObjectValue build() {
        return new TestObjectValue(Objects.requireNonNull(getId(), "Id is missing"),
                Objects.requireNonNull(getValue(), "Value is missing"));
    }
}
