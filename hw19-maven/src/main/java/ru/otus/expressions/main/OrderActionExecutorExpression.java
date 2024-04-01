package ru.otus.expressions.main;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.expressions.services.ActionService;
import ru.otus.expressions.services.ExpressionService;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;
import ru.otus.openapi.model.OrderActionRequest;
import ru.otus.order.OrderActionServiceImpl;


@ExpressionsComponent(
        expression = Expressions.DO_ACTION,
        scopePackages = ScopePackages.DEFAULT,
        description = "Выполнить действие над заказом")
@AllArgsConstructor
public class OrderActionExecutorExpression implements ExpressionFactory {

    private final ActionService actionService;
    private final ExpressionService expressionService;

    @Override
    public Expression create(Object... args) {
        return context -> {
            OrderActionRequest action = (OrderActionRequest) context.get(OrderActionServiceImpl.REQUEST_PARAMETER_NAME);
            Long actionId = action.getAction();
            Expressions expression = actionService.getActionExpressionsById(actionId);
            expressionService.resolve(expression).interpret(context);
        };
    }
}
