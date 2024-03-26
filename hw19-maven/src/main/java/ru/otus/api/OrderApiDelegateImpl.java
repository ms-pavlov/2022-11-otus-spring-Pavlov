package ru.otus.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.otus.openapi.api.OrderApiDelegate;
import ru.otus.openapi.model.OrderAction;
import ru.otus.openapi.model.OrderActionResponse;

@Service
public class OrderApiDelegateImpl implements OrderApiDelegate {

    @Override
    public Mono<ResponseEntity<OrderActionResponse>> processMessages(Mono<OrderAction> orderAction, ServerWebExchange exchange) {
        return OrderApiDelegate.super.processMessages(orderAction, exchange);
    }
}
