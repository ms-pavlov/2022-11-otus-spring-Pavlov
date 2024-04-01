package ru.otus.expressions.other;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.expressions.services.ExpressionService;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;

@ExpressionsComponent(
        expression = Expressions.GET_ORDERS,
        scopePackages = ScopePackages.DEFAULT)
@AllArgsConstructor
public class UserOrdersExpression implements ExpressionFactory {

    private final ExpressionService expressionService;

    @Override
    public Expression create(Object... args) {
        return context ->
                expressionService.resolve(Expressions.DO_EXPRESSIONS,
                                expressionService.resolve(Expressions.GET_ALL_ORDERS),
                                expressionService.resolve(Expressions.ORDERS_FILTER))
                        .interpret(context);
    }
}
