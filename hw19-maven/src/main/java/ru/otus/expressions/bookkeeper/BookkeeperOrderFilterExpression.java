package ru.otus.expressions.bookkeeper;

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
import ru.otus.order.Answer;
import ru.otus.order.OrderActionServiceImpl;

import java.util.List;

@ExpressionsComponent(
        expression = Expressions.ORDERS_FILTER,
        scopePackages = ScopePackages.GET_ORDERS_FOR_BOOKKEEPER,
        description = "Создать заказ от имени простого пользователя")
@AllArgsConstructor
public class BookkeeperOrderFilterExpression implements ExpressionFactory {
    @Override
    public Expression create(Object... args) {
        return context -> {
            List<OrderResponse> orders = (List<OrderResponse>) context.get(OrdersExpression.ORDERS_PARAMETER_NAME);
            Answer<OrderActionResponse> answer = (Answer<OrderActionResponse>) context.get(OrderActionServiceImpl.ANSWER_CONSUMER_NAME);
            answer.ans(new OrderActionResponse()
                    .status("Ok")
                    .putMessageItem("orders", orders.stream()
                            .filter(order -> order.getOrderStatus() == OrderStatuses.FOR_PAYMENT)
                            .toList()));
        };
    }
}
