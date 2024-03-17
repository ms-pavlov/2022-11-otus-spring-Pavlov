package ru.otus.postgre.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.postgre.entities.Genre;

public interface GenresRepository extends JpaRepository<Genre, Long> {

}
