package ru.otus.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.mongo.entities.Comment;

public interface CommentsMongoRepository extends MongoRepository<Comment, String> {

}
