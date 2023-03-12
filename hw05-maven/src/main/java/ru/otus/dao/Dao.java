package ru.otus.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> getById(long id);

    List<T> getAll();

    T create(T entity);

    T update(T entity);

    void delete(long id);

    boolean exist(long id);

    int count();
}
