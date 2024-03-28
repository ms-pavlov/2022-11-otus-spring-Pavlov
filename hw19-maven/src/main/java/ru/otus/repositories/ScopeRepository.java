package ru.otus.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.model.entities.Scope;

public interface ScopeRepository extends ReactiveCrudRepository<Scope, String> {
}
