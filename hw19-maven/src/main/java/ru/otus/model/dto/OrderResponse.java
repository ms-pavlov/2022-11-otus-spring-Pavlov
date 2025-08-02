package ru.otus.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.model.entities.OrderItem;
import ru.otus.model.enums.OrderStatuses;
import ru.otus.openapi.model.UserResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private String id;

    private OrderStatuses orderStatus;
    private UserResponse customer;
    private UserResponse manager;
    private List<OrderItem> orderItems;
}
