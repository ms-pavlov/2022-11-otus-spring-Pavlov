package ru.otus.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.dto.responses.BooksResponse;

public interface GenresService {

    Flux<BooksResponse> findByGenre(Mono<String> name);
}
