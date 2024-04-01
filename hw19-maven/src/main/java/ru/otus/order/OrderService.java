package ru.otus.order;

import ru.otus.model.dto.OrderResponse;
import ru.otus.model.entities.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Optional<Order> findById(String id);

    OrderResponse save(Order entity);

    List<OrderResponse> findAll();
}
