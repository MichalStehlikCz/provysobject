package com.provys.provysobject.impl;

import com.provys.common.exception.InternalException;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TestNmObjectValueBuilder extends
    ProvysNmObjectValueBuilder<TestNmObjectValueBuilder, TestNmObjectValue> {

  private @Nullable String value;
  private boolean updValue = false;

  TestNmObjectValueBuilder() {
  }

  TestNmObjectValueBuilder(TestNmObjectValue value) {
    super(value);
    this.value = value.getValue();
    this.updValue = true;
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
  public TestNmObjectValueBuilder setValue(@Nullable String value) {
    this.value = value;
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
  public TestNmObjectValueBuilder setUpdValue(boolean updValue) {
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
  public TestNmObjectValueBuilder copy() {
    return new TestNmObjectValueBuilder(this);
  }

  @Override
  public TestNmObjectValueBuilder apply(TestNmObjectValue source) {
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
  public TestNmObjectValue build() {
    var id= getId();
    if (id == null) {
      throw new InternalException("Cannot create TestNmObjectValue - Id is missing");
    }
    var nameNm = getNameNm();
    if (nameNm == null) {
      throw new InternalException("Cannot create TestNmObjectValue - nameNm is missing");
    }
    return new TestNmObjectValue(id, nameNm, getValue());
  }

  @Override
  public String toString() {
    return "TestNmObjectValueBuilder{"
        + "value='" + value + '\''
        + ", updValue=" + updValue
        + ", " + super.toString() + '}';
  }
}
