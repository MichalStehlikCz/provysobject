package com.provys.provysobject.impl;

import com.provys.provysobject.ProvysNmObject;

import java.util.Optional;

/**
 * ProvysObject interface used for test of ProvysNmObject related classes
 */
public interface TestNmObject extends ProvysNmObject {
    @SuppressWarnings("unused")
    Optional<String> getValue();
}
