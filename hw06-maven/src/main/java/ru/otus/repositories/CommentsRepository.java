package ru.otus.repositories;

import ru.otus.entities.Comment;

import java.util.List;

public interface CommentsRepository extends Repository<Comment, Long>{

    List<Comment> getCommentsByBookId(Long bookId);
}
