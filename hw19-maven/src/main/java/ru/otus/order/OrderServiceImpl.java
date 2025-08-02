package ru.otus.order;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.mappers.OrderMapper;
import ru.otus.model.dto.OrderResponse;
import ru.otus.model.entities.Order;
import ru.otus.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public Optional<Order> findById(String id) {
        return orderRepository.findById(id);
    }

    @Override
    public OrderResponse save(Order entity) {
        return orderMapper.toDto(orderRepository.save(entity));
    }

    @Override
    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }
}
