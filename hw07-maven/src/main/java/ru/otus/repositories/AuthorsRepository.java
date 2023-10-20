package ru.otus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.entities.Author;


public interface AuthorsRepository extends JpaRepository<Author, Long> {

    Author findByName(String name);

}

