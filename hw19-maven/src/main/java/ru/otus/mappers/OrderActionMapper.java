package ru.otus.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.otus.model.entities.OrderAction;
import ru.otus.model.enums.Expressions;
import ru.otus.openapi.model.ActionRequest;
import ru.otus.openapi.model.ActionResponse;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface OrderActionMapper {

    @Mappings({
            @Mapping(target = "expression", expression = "java(mapExpression(request))")
    })
    OrderAction create(ActionRequest request);

    @Mappings({
            @Mapping(target = "expression", source = "orderAction.expression.expressionName")
    })
    ActionResponse toDto(OrderAction orderAction);

    default Expressions mapExpression(ActionRequest request) {
        return Optional.ofNullable(request)
                .map(ActionRequest::getExpression)
                .map(Expressions::getByName)
                .orElseThrow(() -> new RuntimeException("Не известное выражение"));
    }
}
