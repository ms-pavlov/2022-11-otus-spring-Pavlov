package ru.otus.order;

import ru.otus.openapi.model.OrderActionRequest;
import ru.otus.openapi.model.OrderActionResponse;

public interface OrderActionService {

    OrderActionResponse processMessages(OrderActionRequest orderAction);
}
