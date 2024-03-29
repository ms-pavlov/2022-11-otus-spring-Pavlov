package ru.otus.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.expressions.services.ActionService;
import ru.otus.openapi.api.ActionApiDelegate;
import ru.otus.openapi.model.ActionRequest;
import ru.otus.openapi.model.ActionResponse;

@Service
@AllArgsConstructor
public class ActionApiDelegateImpl implements ActionApiDelegate {

    private final ActionService actionService;

    @Override
    public Mono<ResponseEntity<ActionResponse>> addAction(Mono<ActionRequest> actionRequest, ServerWebExchange exchange) {
        return actionRequest.flatMap(actionService::addAction)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<ActionResponse>>> getAll(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(actionService.getAll()));
    }
}
