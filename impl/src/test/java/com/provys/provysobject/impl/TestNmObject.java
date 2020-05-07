package com.provys.provysobject.impl;

import com.provys.provysobject.ProvysNmObject;

import java.util.Optional;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * ProvysObject interface used for test of ProvysNmObject related classes.
 */
public interface TestNmObject extends ProvysNmObject {

  /**
   * Property Value.
   *
   * @return value of property value
   */
  @Nullable String getValue();
}
