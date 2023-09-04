package ru.otus.repositories;

import ru.otus.entities.Genre;

import java.util.List;

public interface GenresRepository extends Repository<Genre, Long>{

    List<Genre> findByBookId(Long id);

    Genre findByName(String name);
}
