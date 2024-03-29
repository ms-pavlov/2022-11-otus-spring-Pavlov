package ru.otus.order;

import reactor.core.publisher.Mono;
import ru.otus.openapi.model.OrderAction;
import ru.otus.openapi.model.OrderActionResponse;

public interface OrderService {

    Mono<OrderActionResponse> processMessages(OrderAction orderAction);
}
