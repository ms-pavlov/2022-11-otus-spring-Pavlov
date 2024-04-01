package ru.otus.expressions.user;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.expressions.other.OrdersExpression;
import ru.otus.model.dto.OrderResponse;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;
import ru.otus.openapi.model.OrderActionResponse;
import ru.otus.openapi.model.UserResponse;
import ru.otus.order.Answer;
import ru.otus.order.OrderActionServiceImpl;

import java.util.List;
import java.util.Objects;

@ExpressionsComponent(
        expression = Expressions.ORDERS_FILTER,
        scopePackages = ScopePackages.GET_ORDERS_FOR_USER,
        description = "Создать заказ от имени простого пользователя")
@AllArgsConstructor
public class UserOrderFilterExpression implements ExpressionFactory {
    @Override
    public Expression create(Object... args) {
        return context -> {
            List<OrderResponse> orders = (List<OrderResponse>) context.get(OrdersExpression.ORDERS_PARAMETER_NAME);
            UserResponse currentUser = (UserResponse) context.get(OrderActionServiceImpl.CURRENT_USER_PARAMETER_NAME);
            Answer<OrderActionResponse> answer = (Answer<OrderActionResponse>) context.get(OrderActionServiceImpl.ANSWER_CONSUMER_NAME);
            answer.ans(new OrderActionResponse()
                    .status("Ok")
                    .putMessageItem("orders", orders.stream()
                            .filter(order -> Objects.equals(order.getCustomer().getLogin(), currentUser.getLogin()))
                            .toList()));
        };
    }
}
