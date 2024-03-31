package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.model.entities.OrderAction;

import java.util.Optional;

public interface OrderActionRepository extends MongoRepository<OrderAction, String> {

    Optional<OrderAction> findByActionId(Long id);

}
