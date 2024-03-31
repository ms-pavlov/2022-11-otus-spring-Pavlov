package ru.otus.expressions.other;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;
import ru.otus.openapi.model.OrderActionRequest;
import ru.otus.order.OrderActionServiceImpl;
import ru.otus.repositories.OrderRepository;

@ExpressionsComponent(
        expression = Expressions.GET_ORDER_BY_ID,
        scopePackages = {ScopePackages.CHENG_ORDER_STATUSES},
        description = "Создать заказ от имени простого пользователя")
@AllArgsConstructor
public class OrderGetterExpression implements ExpressionFactory {


    private final OrderRepository orderRepository;

    @Override
    public Expression create(Object... args) {
        return context -> {
            OrderActionRequest action = (OrderActionRequest) context.get(OrderActionServiceImpl.REQUEST_PARAMETER_NAME);
            context.add(OrderParserExpression.ORDER_PARAMETER, orderRepository.findById(action.getOrder())
                    .orElseThrow(() -> new RuntimeException("Не удалось найти заказ " + action.getOrder())));
        };
    }
}
