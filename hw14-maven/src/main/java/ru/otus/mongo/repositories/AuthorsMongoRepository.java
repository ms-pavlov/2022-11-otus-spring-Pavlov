package ru.otus.mongo.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.mongo.entities.Author;

public interface AuthorsMongoRepository extends MongoRepository<Author, String> {

    Author findByName(String name);

}

