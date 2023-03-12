package ru.otus.dao;

import ru.otus.entities.Genres;

import java.util.List;

public interface GenresDao extends Dao<Genres> {
    List<Genres> findByBookId(Long id);

    Genres findByName(String name);
}
