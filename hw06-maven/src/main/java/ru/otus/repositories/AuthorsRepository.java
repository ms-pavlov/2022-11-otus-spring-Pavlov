package ru.otus.repositories;

import ru.otus.entities.Author;

import java.util.List;

public interface AuthorsRepository extends Repository<Author, Long> {

    List<Author> findByBookId(Long id);

    Author findByName(String name);
}

