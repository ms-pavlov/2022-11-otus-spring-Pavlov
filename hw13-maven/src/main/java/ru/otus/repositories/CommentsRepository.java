package ru.otus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.otus.entities.Comment;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.book")
    List<Comment> find();

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.book.id = :id")
    void deleteByBookId(Long id);

    @Query("SELECT CASE WHEN c.owner IS NULL THEN '' ELSE c.owner.login END FROM Comment c WHERE c.id = :id")
    String getOwnerById(Long id);
}
