package com.provys.provysobject;

public interface ProvysObjectRepository {
    <T extends ProvysObject> ProvysObjectManager<T> getManager(Class<T> forEntity);
}
