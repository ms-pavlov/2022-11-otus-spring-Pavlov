package ru.otus.repositories;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.entities.Genre;

import java.util.List;

public interface GenresRepository extends CrudRepository<Genre, Long> {
    Genre findByName(String name);

    @Query("SELECT g FROM Genre g")
    List<Genre> find();
}
