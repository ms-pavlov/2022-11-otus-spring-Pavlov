package ru.otus.expressions;

import ru.otus.annotations.ExpressionsComponent;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;

@ExpressionsComponent(
        expression = Expressions.CREATE_ORDER,
        scopePackages = ScopePackages.CREATE_ORDER_BY_USER,
        description = "Создать заказ от имени простого пользователя")
public class NewOrderExpression implements ExpressionFactory {


    @Override
    public Expression create(Object... args) {
        return context -> {
        };
    }
}
