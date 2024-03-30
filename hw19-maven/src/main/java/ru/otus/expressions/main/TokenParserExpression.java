package ru.otus.expressions.main;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;
import ru.otus.openapi.model.OrderAction;
import ru.otus.securities.TokenService;

import java.util.Optional;

import static ru.otus.expressions.main.OrderActionProcessorExpression.REQUEST_PARAMETER_NAME;

@ExpressionsComponent(
        expression = Expressions.PARSE_TOKEN,
        scopePackages = ScopePackages.DEFAULT,
        description = "Выполнить действие над заказом")
@AllArgsConstructor
public class TokenParserExpression implements ExpressionFactory {

    public static final String TOKEN_PARAMETER_NAME = "token";
    public static final String PARSED_TOKEN_PARAMETER_NAME = "claims";

    private final TokenService tokenService;

    @Override
    public Expression create(Object... args) {
        return context -> {
            OrderAction action = (OrderAction) context.get(REQUEST_PARAMETER_NAME);
            context.add(PARSED_TOKEN_PARAMETER_NAME,
                    Optional.ofNullable(action)
                            .map(OrderAction::getToken)
                            .map(tokenService::parse)
                            .orElseThrow(() -> new RuntimeException("Отсутствует токен")));
        };
    }
}
