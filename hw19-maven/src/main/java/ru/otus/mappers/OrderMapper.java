package ru.otus.mappers;

import org.mapstruct.Mapper;
import ru.otus.model.dto.OrderResponse;
import ru.otus.model.entities.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toDto(Order user);
}
