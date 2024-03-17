package ru.otus.postgre.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.postgre.entities.Author;


public interface AuthorsRepository extends JpaRepository<Author, Long> {

}

