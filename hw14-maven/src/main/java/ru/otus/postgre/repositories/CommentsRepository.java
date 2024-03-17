package ru.otus.postgre.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.postgre.entities.Comment;

public interface CommentsRepository extends JpaRepository<Comment, Long> {

}
