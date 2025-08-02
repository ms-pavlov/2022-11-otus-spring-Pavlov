package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.model.entities.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
}
