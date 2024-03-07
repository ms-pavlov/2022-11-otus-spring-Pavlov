package ru.otus.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.otus.entities.Author;

public interface AuthorsRepository extends ReactiveCrudRepository<Author, String> {

    Mono<Author> findByName(String name);

}

