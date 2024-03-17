package ru.otus.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.mongo.entities.Genre;

public interface GenresMongoRepository extends MongoRepository<Genre, String> {
    Genre findByName(String name);

}
