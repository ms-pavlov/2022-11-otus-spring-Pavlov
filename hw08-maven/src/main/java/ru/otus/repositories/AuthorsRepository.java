package ru.otus.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.entities.Author;

public interface AuthorsRepository extends CrudRepository<Author, String> {

    Author findByName(String name);

}

