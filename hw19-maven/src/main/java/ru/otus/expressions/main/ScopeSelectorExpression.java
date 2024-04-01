package ru.otus.expressions.main;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.expressions.storage.ExpressionStorage;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;

@ExpressionsComponent(
        expression = Expressions.CHANGE_SCOPE,
        scopePackages = ScopePackages.DEFAULT,
        description = "Сменить область видимости")
@AllArgsConstructor
public class ScopeSelectorExpression implements ExpressionFactory {

    public static final String SCOPE_PARAMETER_NAME = "scope";

    private final ExpressionStorage expressionStorage;

    @Override
    public Expression create(Object... args) {
        return context -> {
            String scopeName = (String) context.get(SCOPE_PARAMETER_NAME);
            if (null == scopeName) {
                expressionStorage.setDefaultScope();
                return;
            }
            expressionStorage.setScope(scopeName);
        };
    }
}
