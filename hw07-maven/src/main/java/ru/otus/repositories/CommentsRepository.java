package ru.otus.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.entities.Comment;

import java.util.List;

public interface CommentsRepository extends CrudRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.book")
    List<Comment> find();

}
