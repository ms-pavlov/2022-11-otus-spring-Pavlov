package ru.otus.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.otus.openapi.api.TokenApiDelegate;
import ru.otus.openapi.model.TokenResponse;
import ru.otus.securities.TokenFactory;
import ru.otus.securities.UsersService;

import java.security.Principal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenApiDelegateImpl implements TokenApiDelegate {

    private final TokenFactory tokenFactory;
    private final UsersService usersService;

    @Override
    public Mono<ResponseEntity<TokenResponse>> getToken(String scope, ServerWebExchange exchange) {
        return exchange.getPrincipal()
                .flatMap(
                        principal -> Optional.ofNullable(principal)
                                .map(Principal::getName)
                                .map(usersService::getUser)
                                .map(user -> user
                                        .map(value -> tokenFactory.create(scope, value))
                                        .map(token -> new TokenResponse().scope(scope).token(token))
                                        .map(ResponseEntity::ok))
                                .orElseGet(() -> Mono.just(ResponseEntity.badRequest().build())));
    }
}
