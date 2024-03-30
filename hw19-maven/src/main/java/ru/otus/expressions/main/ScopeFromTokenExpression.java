package ru.otus.expressions.main;

import io.jsonwebtoken.Claims;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;

import static ru.otus.expressions.main.ScopeSelectorExpression.SCOPE_PARAMETER_NAME;
import static ru.otus.expressions.main.TokenParserExpression.PARSED_TOKEN_PARAMETER_NAME;

@ExpressionsComponent(
        expression = Expressions.GET_CURRENT_SCOPE,
        scopePackages = ScopePackages.DEFAULT)
public class ScopeFromTokenExpression implements ExpressionFactory {
    @Override
    public Expression create(Object... args) {
        return context -> {
            Claims claims = (Claims) context.get(PARSED_TOKEN_PARAMETER_NAME);
            context.add(SCOPE_PARAMETER_NAME, claims.get("scope", String.class));
        };
    }
}
