package ru.otus.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.otus.model.entities.User;

public interface UsersRepository extends ReactiveCrudRepository<User, String> {

    Mono<User> findByLogin(String username);

    Mono<Boolean> existsByLogin(String username);
}
