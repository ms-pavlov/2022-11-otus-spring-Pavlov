package ru.otus.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.otus.model.entities.Scope;

public interface ScopeRepository extends ReactiveCrudRepository<Scope, String> {

    Mono<Scope> findByName(String scope);
}
