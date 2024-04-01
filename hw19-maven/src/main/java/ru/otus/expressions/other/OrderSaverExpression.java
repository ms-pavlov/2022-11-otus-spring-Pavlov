package ru.otus.expressions.other;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.entities.Order;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;
import ru.otus.openapi.model.OrderActionResponse;
import ru.otus.order.Answer;
import ru.otus.order.OrderActionServiceImpl;
import ru.otus.order.OrderService;

import static ru.otus.expressions.other.OrderParserExpression.ORDER_PARAMETER;

@ExpressionsComponent(
        expression = Expressions.SAVE_ORDER,
        scopePackages = {
                ScopePackages.CREATE_ORDER_BY_USER,
                ScopePackages.CREATE_ORDER_BY_VIP_USER,
                ScopePackages.CHENG_ORDER_STATUSES_FOR_MANAGER,
                ScopePackages.CHENG_ORDER_STATUSES_FOR_BOOKKEEPER},
        description = "Создать заказ от имени простого пользователя")
@AllArgsConstructor
public class OrderSaverExpression implements ExpressionFactory {

    private final OrderService orderService;

    @Override
    public Expression create(Object... args) {
        return context -> {
            Order order = (Order) context.get(ORDER_PARAMETER);
            orderService.save(order);
            Answer<OrderActionResponse> answer = (Answer<OrderActionResponse>) context.get(OrderActionServiceImpl.ANSWER_CONSUMER_NAME);
            answer.ans(new OrderActionResponse().putMessageItem("done", "all"));
        };
    }
}
