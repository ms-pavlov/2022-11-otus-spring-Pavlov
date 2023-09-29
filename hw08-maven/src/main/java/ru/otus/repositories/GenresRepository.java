package ru.otus.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.entities.Genre;

public interface GenresRepository extends CrudRepository<Genre, String> {
    Genre findByName(String name);

}
