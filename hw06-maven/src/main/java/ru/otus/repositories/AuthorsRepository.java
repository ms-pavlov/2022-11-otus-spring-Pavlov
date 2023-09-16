package ru.otus.repositories;

import ru.otus.entities.Author;

public interface AuthorsRepository extends Repository<Author, Long> {

    Author getByName(String name);

    boolean existName(String name);
}

