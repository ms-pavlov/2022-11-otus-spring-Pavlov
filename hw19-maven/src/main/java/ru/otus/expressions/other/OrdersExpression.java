package ru.otus.expressions.other;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;
import ru.otus.order.OrderService;

@ExpressionsComponent(
        expression = Expressions.GET_ALL_ORDERS,
        scopePackages = ScopePackages.DEFAULT)
@AllArgsConstructor
public class OrdersExpression implements ExpressionFactory {

    public final static String ORDERS_PARAMETER_NAME = "ordersList";

    private final OrderService orderService;

    @Override
    public Expression create(Object... args) {
        return context -> context.add(ORDERS_PARAMETER_NAME, orderService.findAll());
    }
}
