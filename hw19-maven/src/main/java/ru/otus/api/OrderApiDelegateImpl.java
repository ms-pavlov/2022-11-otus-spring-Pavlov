package ru.otus.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.otus.openapi.api.OrderApiDelegate;
import ru.otus.openapi.model.OrderActionRequest;
import ru.otus.openapi.model.OrderActionResponse;
import ru.otus.order.OrderActionService;

@Service
@AllArgsConstructor
public class OrderApiDelegateImpl implements OrderApiDelegate {

    private final OrderActionService orderActionService;

    @Override
    public ResponseEntity<OrderActionResponse> processMessages(OrderActionRequest orderAction) {
        return ResponseEntity.ok(orderActionService.processMessages(orderAction));
    }
}
