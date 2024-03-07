package ru.otus.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.otus.entities.Genre;

public interface GenresRepository extends ReactiveCrudRepository<Genre, String> {
    Mono<Genre> findByName(String name);

}
