package ru.otus.services;

import reactor.core.publisher.Mono;
import ru.otus.dto.responses.AuthorsResponse;

public interface AuthorsService {

    Mono<AuthorsResponse> findById(Mono<String> id);
}
