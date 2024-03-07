package ru.otus.services;

import reactor.core.publisher.Mono;
import ru.otus.dto.responses.GenresResponse;

public interface GenresService {

    Mono<GenresResponse> findById(Mono<String> id);
}
