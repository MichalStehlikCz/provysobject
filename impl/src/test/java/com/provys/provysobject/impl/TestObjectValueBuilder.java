package com.provys.provysobject.impl;

import com.provys.common.exception.InternalException;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;

class TestObjectValueBuilder
    extends ProvysObjectValueBuilder<TestObjectValueBuilder, TestObjectValue> {

  private @Nullable String value;
  private boolean updValue = false;

  TestObjectValueBuilder() {
  }

  TestObjectValueBuilder(TestObjectValue value) {
    super(value);
    this.value = value.getValue();
    this.updValue = true;
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
   * Value of field value.
   *
   * @return value of field value
   */
  public @Nullable String getValue() {
    return value;
  }

  /**
   * Set value of field value.
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
   * Value of field updValue.
   *
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
   * Clone builder - create new builder with exactly same set of modified properties.
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
   * Build new value object based on values in this builder. Does validation first. Defaults are
   * used for properties that has not been set.
   *
   * @return new value object
   */
  @Override
  public TestObjectValue build() {
    var id = getId();
    if (id == null) {
      throw new InternalException("Cannot create TestObjectValue - id is missing");
    }
    if (value == null) {
      throw new InternalException("Cannot create TestObjectValue - value is missing");
    }
    return new TestObjectValue(id, value);
  }

    @Override
    public String toString() {
        return "TestObjectValueBuilder{"
            + "value='" + value + '\''
            + ", updValue=" + updValue
            + ", " + super.toString() + '}';
    }
}
