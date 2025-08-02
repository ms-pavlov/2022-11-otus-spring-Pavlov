package ru.otus.expressions.manager;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.entities.Order;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;
import ru.otus.openapi.model.UserResponse;
import ru.otus.order.OrderActionServiceImpl;

import static ru.otus.expressions.other.OrderParserExpression.ORDER_PARAMETER;

@ExpressionsComponent(
        expression = Expressions.SET_ORDER_MANAGER,
        scopePackages = {ScopePackages.CHENG_ORDER_STATUSES_FOR_MANAGER},
        description = "Создать заказ от имени простого пользователя")
@AllArgsConstructor
public class OrderManagerExpression implements ExpressionFactory {

    @Override
    public Expression create(Object... args) {
        return context -> {
            Order order = (Order) context.get(ORDER_PARAMETER);
            UserResponse user = (UserResponse) context.get(OrderActionServiceImpl.CURRENT_USER_PARAMETER_NAME);
            order.setManager(user);
        };
    }
}
