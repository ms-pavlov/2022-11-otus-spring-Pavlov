package ru.otus.repositories;

import ru.otus.entities.Genre;

public interface GenresRepository extends Repository<Genre, Long>{
    Genre getByName(String name);

    boolean existName(String name);
}
