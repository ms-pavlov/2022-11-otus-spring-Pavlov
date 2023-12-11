package ru.otus.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.entities.Comment;

import java.util.List;

public interface CommentsRepository extends CrudRepository<Comment, String> {

    List<Comment> findByBookId(String bookId);

    void deleteByBookId(String bookId);
}
