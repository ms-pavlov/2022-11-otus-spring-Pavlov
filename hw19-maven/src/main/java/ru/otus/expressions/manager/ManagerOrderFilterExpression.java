package ru.otus.expressions.manager;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.expressions.other.OrdersExpression;
import ru.otus.model.dto.OrderResponse;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.OrderStatuses;
import ru.otus.model.enums.ScopePackages;
import ru.otus.openapi.model.OrderActionResponse;
import ru.otus.openapi.model.UserResponse;
import ru.otus.order.Answer;
import ru.otus.order.OrderActionServiceImpl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ExpressionsComponent(
        expression = Expressions.ORDERS_FILTER,
        scopePackages = ScopePackages.GET_ORDERS_FOR_MANAGER,
        description = "Создать заказ от имени простого пользователя")
@AllArgsConstructor
public class ManagerOrderFilterExpression implements ExpressionFactory {
    @Override
    public Expression create(Object... args) {
        return context -> {
            List<OrderResponse> orders = (List<OrderResponse>) context.get(OrdersExpression.ORDERS_PARAMETER_NAME);
            UserResponse currentUser = (UserResponse) context.get(OrderActionServiceImpl.CURRENT_USER_PARAMETER_NAME);
            Answer<OrderActionResponse> answer = (Answer<OrderActionResponse>) context.get(OrderActionServiceImpl.ANSWER_CONSUMER_NAME);
            answer.ans(new OrderActionResponse()
                    .status("Ok")
                    .putMessageItem("orders", orders.stream()
                            .filter(Objects::nonNull)
                            .filter(order -> Optional.of(order)
                                    .map(OrderResponse::getManager)
                                    .map(UserResponse::getLogin)
                                    .map(manager -> manager.equals(currentUser.getLogin()))
                                    .orElse(false) || order.getOrderStatus() == OrderStatuses.CREATED)
                            .toList()));
        };
    }
}
