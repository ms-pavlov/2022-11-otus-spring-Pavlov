package ru.otus.dao;

import ru.otus.entities.Authors;

import java.util.List;

public interface AuthorsDao extends Dao<Authors> {

    List<Authors> findByBookId(Long id);

    Authors findByName(String name);
}
