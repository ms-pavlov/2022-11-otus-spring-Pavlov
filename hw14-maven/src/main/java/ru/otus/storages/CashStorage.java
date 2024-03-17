package ru.otus.storages;

public interface CashStorage<T, ID> {

    void put(ID id, T object);

    T get(ID id);
}
