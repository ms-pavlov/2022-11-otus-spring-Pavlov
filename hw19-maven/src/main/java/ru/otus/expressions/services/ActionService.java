package ru.otus.expressions.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.openapi.model.ActionRequest;
import ru.otus.openapi.model.ActionResponse;

public interface ActionService {

    Mono<ActionResponse> addAction(ActionRequest actionRequest);

    Flux<ActionResponse> getAll();

}
