package ru.otus.expressions.main;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;
import ru.otus.openapi.model.OrderActionRequest;
import ru.otus.order.OrderActionServiceImpl;
import ru.otus.securities.TokenService;

import java.util.Optional;

@ExpressionsComponent(
        expression = Expressions.PARSE_TOKEN,
        scopePackages = ScopePackages.DEFAULT,
        description = "Выполнить действие над заказом")
@AllArgsConstructor
public class TokenParserExpression implements ExpressionFactory {

    public static final String PARSED_TOKEN_PARAMETER_NAME = "claims";

    private final TokenService tokenService;

    @Override
    public Expression create(Object... args) {
        return context -> {
            OrderActionRequest action = (OrderActionRequest) context.get(OrderActionServiceImpl.REQUEST_PARAMETER_NAME);
            context.add(PARSED_TOKEN_PARAMETER_NAME,
                    Optional.ofNullable(action)
                            .map(OrderActionRequest::getToken)
                            .map(tokenService::parse)
                            .orElseThrow(() -> new RuntimeException("Отсутствует токен")));
        };
    }
}
