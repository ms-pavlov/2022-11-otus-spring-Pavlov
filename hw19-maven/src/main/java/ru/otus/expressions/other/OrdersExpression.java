package ru.otus.expressions.other;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;
import ru.otus.openapi.model.OrderActionResponse;
import ru.otus.order.Answer;
import ru.otus.order.OrderActionServiceImpl;
import ru.otus.repositories.OrderRepository;

@ExpressionsComponent(
        expression = Expressions.GET_ORDERS,
        scopePackages = ScopePackages.DEFAULT)
@AllArgsConstructor
public class OrdersExpression implements ExpressionFactory {

    private final OrderRepository orderRepository;

    @Override
    public Expression create(Object... args) {


        return context -> {
            Answer<OrderActionResponse> answer = (Answer<OrderActionResponse>) context.get(OrderActionServiceImpl.ANSWER_CONSUMER_NAME);
            answer.ans(new OrderActionResponse()
                    .status("Ok")
                    .putMessageItem("orders", orderRepository.findAll()));
        };
    }
}
