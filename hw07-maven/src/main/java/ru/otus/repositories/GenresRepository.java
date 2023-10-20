package ru.otus.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.entities.Genre;

public interface GenresRepository extends JpaRepository<Genre, Long> {
    Genre findByName(String name);

}
