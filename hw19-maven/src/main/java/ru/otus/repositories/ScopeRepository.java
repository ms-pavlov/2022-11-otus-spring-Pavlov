package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.model.entities.Scope;

public interface ScopeRepository extends MongoRepository<Scope, String> {

    Scope findByName(String scope);
}
