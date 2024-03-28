package ru.otus.securities;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.openapi.model.ScopeRequest;
import ru.otus.openapi.model.ScopeResponse;

public interface ScopeService {

    Mono<ScopeResponse> createScopes(Mono<ScopeRequest> scopeRequest);

    Flux<ScopeResponse> getAll();
}
