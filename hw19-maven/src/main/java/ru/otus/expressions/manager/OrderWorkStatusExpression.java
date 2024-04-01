package ru.otus.expressions.manager;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.expressions.services.ExpressionService;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.OrderStatuses;
import ru.otus.model.enums.ScopePackages;

@ExpressionsComponent(
        expression = Expressions.GET_ORDERS_IN_WORK,
        scopePackages = ScopePackages.CHENG_ORDER_STATUSES_FOR_MANAGER)
@AllArgsConstructor
public class OrderWorkStatusExpression implements ExpressionFactory {

    private final ExpressionService expressionService;

    @Override
    public Expression create(Object... args) {
        return context ->
                expressionService.resolve(Expressions.DO_EXPRESSIONS,
                                expressionService.resolve(Expressions.GET_ORDER_BY_ID),
                                expressionService.resolve(Expressions.VALID_ORDER_STATUS, OrderStatuses.CREATED),
                                expressionService.resolve(Expressions.SET_ORDER_STATUS, OrderStatuses.AT_WORK),
                                expressionService.resolve(Expressions.SET_ORDER_MANAGER),
                                expressionService.resolve(Expressions.SAVE_ORDER))
                        .interpret(context);
    }
}
