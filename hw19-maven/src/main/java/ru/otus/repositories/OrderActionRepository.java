package ru.otus.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.otus.model.entities.OrderAction;

public interface OrderActionRepository extends ReactiveCrudRepository<OrderAction, String> {

    Mono<OrderAction> findByActionId(Long id);

}
