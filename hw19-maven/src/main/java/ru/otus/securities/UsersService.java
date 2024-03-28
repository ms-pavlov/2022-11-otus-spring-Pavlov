package ru.otus.securities;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.model.entities.User;
import ru.otus.openapi.model.UserRequest;
import ru.otus.openapi.model.UserResponse;

public interface UsersService {

    Mono<User> getUser(String username);

    Mono<UserResponse> create(UserRequest user);

    Mono<Boolean> existsByUsername(String username);

    Flux<UserResponse> getUsers();

    Mono<UserResponse> update(String id, UserRequest request);
}
