package ru.otus.expressions.main;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.expressions.services.PackageService;
import ru.otus.expressions.storage.ExpressionStorage;
import ru.otus.model.entities.Scope;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;

@ExpressionsComponent(
        expression = Expressions.RESOLVE_EXPRESSIONS,
        scopePackages = ScopePackages.DEFAULT,
        description = "Выполнить действие над заказом")
@AllArgsConstructor
public class ExpressionResolverExpression implements ExpressionFactory {

    public final static String SCOPE_INFO_PARAMETER = "scope_info";

    private final ExpressionStorage expressionStorage;
    private final PackageService packageService;

    @Override
    public Expression create(Object... args) {
        return context -> {
            Scope scope = (Scope) context.get(SCOPE_INFO_PARAMETER);
            if (null != scope) {
                scope.getPackages()
                        .stream()
                        .map(packageService::getPackageExpressions)
                        .forEach(expressionStorage::put);
            }
        };
    }
}
