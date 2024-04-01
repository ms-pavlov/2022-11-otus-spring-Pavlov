package ru.otus.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.model.enums.OrderStatuses;
import ru.otus.openapi.model.UserResponse;

import java.util.List;

@Document(collection = "order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    private String id;

    private OrderStatuses orderStatus;
    private UserResponse customer;
    private UserResponse manager;
    private List<OrderItem> orderItems;
}
