package ru.otus.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.otus.openapi.api.OrderApiDelegate;
import ru.otus.openapi.model.OrderAction;
import ru.otus.openapi.model.OrderActionResponse;
import ru.otus.order.OrderService;

@Service
@AllArgsConstructor
public class OrderApiDelegateImpl implements OrderApiDelegate {

    private final OrderService orderService;

    @Override
    public Mono<ResponseEntity<OrderActionResponse>> processMessages(Mono<OrderAction> orderAction, ServerWebExchange exchange) {
        return orderAction
                .flatMap(orderService::processMessages)
                .map(ResponseEntity::ok);
    }
}
