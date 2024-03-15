package ru.otus.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.entities.Comment;

public interface CommentsRepository extends ReactiveCrudRepository<Comment, String> {

    Flux<Comment> findByBookId(String bookId);

    Mono<Void> deleteByBookId(String bookId);
}
