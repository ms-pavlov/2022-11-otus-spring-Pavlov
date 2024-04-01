package ru.otus.expressions.other;

import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.entities.Order;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.OrderStatuses;
import ru.otus.model.enums.ScopePackages;

import java.util.Arrays;
import java.util.List;

import static ru.otus.expressions.other.OrderParserExpression.ORDER_PARAMETER;

@ExpressionsComponent(
        expression = Expressions.VALID_ORDER_STATUS,
        scopePackages = {ScopePackages.CHENG_ORDER_STATUSES_FOR_MANAGER, ScopePackages.CHENG_ORDER_STATUSES_FOR_BOOKKEEPER},
        description = "Создать заказ от имени простого пользователя")
public class OrderStatusValidatorExpression implements ExpressionFactory {
    @Override
    public Expression create(Object... args) {
        if (args.length < 1) {
            throw new RuntimeException("Не заданы доступные статусы для заказа");
        }
        return context -> {
            List<OrderStatuses> orderStatuses = Arrays.stream(args)
                    .map(object -> (OrderStatuses) object)
                    .toList();
            Order order = (Order) context.get(ORDER_PARAMETER);
            if (!orderStatuses.contains(order.getOrderStatus())) {
                throw new RuntimeException("Новый статус можно выставить только заказ со статусом " + orderStatuses);
            }
        };
    }
}
