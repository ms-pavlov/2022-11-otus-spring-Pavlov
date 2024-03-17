package ru.otus.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.mongo.entities.Book;

public interface BooksMongoRepository extends MongoRepository<Book, String> {

}
