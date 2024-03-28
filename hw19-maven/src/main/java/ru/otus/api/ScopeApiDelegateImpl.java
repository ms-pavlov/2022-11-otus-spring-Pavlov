package ru.otus.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.openapi.api.ScopeApiDelegate;
import ru.otus.openapi.model.ScopeRequest;
import ru.otus.openapi.model.ScopeResponse;
import ru.otus.securities.ScopeService;

@Service
@AllArgsConstructor
public class ScopeApiDelegateImpl implements ScopeApiDelegate {

    private final ScopeService scopeService;

    @Override
    public Mono<ResponseEntity<ScopeResponse>> createScopes(Mono<ScopeRequest> scopeRequest, ServerWebExchange exchange) {
        return scopeService.createScopes(scopeRequest)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<ScopeResponse>>> getScopes(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(scopeService.getAll()));
    }
}
