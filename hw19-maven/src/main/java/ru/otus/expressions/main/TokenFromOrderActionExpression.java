package ru.otus.expressions.main;

import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;
import ru.otus.openapi.model.OrderAction;

import static ru.otus.expressions.main.OrderActionProcessorExpression.REQUEST_PARAMETER_NAME;
import static ru.otus.expressions.main.TokenParserExpression.TOKEN_PARAMETER_NAME;

@ExpressionsComponent(
        expression = Expressions.GET_TOKEN_FOR_ORDER_ACTION,
        scopePackages = ScopePackages.DEFAULT)
public class TokenFromOrderActionExpression implements ExpressionFactory {
    @Override
    public Expression create(Object... args) {
        return context -> {
            OrderAction action = (OrderAction) context.get(REQUEST_PARAMETER_NAME);
            context.add(TOKEN_PARAMETER_NAME, action.getToken());
        };
    }
}
