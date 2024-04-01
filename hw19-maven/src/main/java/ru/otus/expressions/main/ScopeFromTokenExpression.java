package ru.otus.expressions.main;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.expressions.services.ScopeService;
import ru.otus.model.entities.Scope;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;

import static ru.otus.expressions.main.ExpressionResolverExpression.SCOPE_INFO_PARAMETER;
import static ru.otus.expressions.main.ScopeSelectorExpression.SCOPE_PARAMETER_NAME;
import static ru.otus.expressions.main.TokenParserExpression.PARSED_TOKEN_PARAMETER_NAME;

@ExpressionsComponent(
        expression = Expressions.GET_CURRENT_SCOPE,
        scopePackages = ScopePackages.DEFAULT)
@AllArgsConstructor
public class ScopeFromTokenExpression implements ExpressionFactory {

    private final ScopeService scopeService;

    @Override
    public Expression create(Object... args) {
        return context -> {
            Claims claims = (Claims) context.get(PARSED_TOKEN_PARAMETER_NAME);
            String scopeName = claims.get("scope", String.class);
            Scope scope = scopeService.getByName(scopeName);
            context.add(SCOPE_INFO_PARAMETER, scope);
            context.add(SCOPE_PARAMETER_NAME, scopeName);
        };
    }
}
