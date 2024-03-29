package ru.otus.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.openapi.api.UserApiDelegate;
import ru.otus.openapi.model.UserRequest;
import ru.otus.openapi.model.UserResponse;
import ru.otus.securities.services.UsersService;

@Service
@AllArgsConstructor
public class UserApiDelegateImpl implements UserApiDelegate {

    private final UsersService usersService;

    @Override
    public Mono<ResponseEntity<UserResponse>> createUser(Mono<UserRequest> userRequest, ServerWebExchange exchange) {
        return userRequest
                .flatMap(usersService::create)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<UserResponse>>> getUsers(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(usersService.getUsers()));
    }

    @Override
    public Mono<ResponseEntity<UserResponse>> updateUser(String id, Mono<UserRequest> userRequest, ServerWebExchange exchange) {
        return userRequest.flatMap(request -> usersService.update(id, request))
                .map(ResponseEntity::ok);
    }
}
