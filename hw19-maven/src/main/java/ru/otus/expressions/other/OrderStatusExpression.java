package ru.otus.expressions.other;

import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.entities.Order;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.OrderStatuses;
import ru.otus.model.enums.ScopePackages;

import static ru.otus.expressions.other.OrderParserExpression.ORDER_PARAMETER;

@ExpressionsComponent(
        expression = Expressions.SET_ORDER_STATUS,
        scopePackages = {
                ScopePackages.CREATE_ORDER_BY_USER,
                ScopePackages.CREATE_ORDER_BY_VIP_USER,
                ScopePackages.CHENG_ORDER_STATUSES_FOR_MANAGER,
                ScopePackages.CHENG_ORDER_STATUSES_FOR_BOOKKEEPER},
        description = "Создать заказ от имени простого пользователя")
public class OrderStatusExpression implements ExpressionFactory {
    @Override
    public Expression create(Object... args) {
        if (args.length < 1) {
            throw new RuntimeException("Не задан статус для заказа");
        }
        return context -> {
            Order order = (Order) context.get(ORDER_PARAMETER);
            order.setOrderStatus((OrderStatuses) args[0]);
        };
    }
}
