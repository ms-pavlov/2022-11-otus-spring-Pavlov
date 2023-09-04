package ru.otus.repositories;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {

    Optional<T> getById(long id);

    List<T> getAll();

    T create(T entity);

    T update(T entity);

    void delete(ID id);

    boolean exist(ID id);

    int count();
}
