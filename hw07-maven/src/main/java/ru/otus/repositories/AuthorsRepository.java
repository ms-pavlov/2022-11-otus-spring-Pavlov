package ru.otus.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.entities.Author;

import java.util.List;


public interface AuthorsRepository extends CrudRepository<Author, Long> {

    Author findByName(String name);

    @Query("SELECT a FROM Author a")
    List<Author> find();
}

