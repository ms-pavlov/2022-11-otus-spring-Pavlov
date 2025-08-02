package ru.otus.expressions.main;

import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;

@ExpressionsComponent(
        expression = Expressions.DO_EXPRESSIONS,
        scopePackages = ScopePackages.DEFAULT,
        description = "Выполнить действие над заказом")
public class MultiExpression implements ExpressionFactory {
    @Override
    public Expression create(Object... args) {
        return context -> {
            for (Object item : args) {
                ((Expression) item).interpret(context);
            }
        };
    }
}
