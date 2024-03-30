package ru.otus.expressions.main;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.expressions.services.ExpressionService;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;
import ru.otus.openapi.model.OrderActionResponse;
import ru.otus.order.Answer;

@ExpressionsComponent(
        expression = Expressions.PROCESS_ORDER_MESSAGE,
        scopePackages = ScopePackages.DEFAULT,
        description = "Выполнить действие над заказом")
@AllArgsConstructor
public class OrderActionProcessorExpression implements ExpressionFactory {

    public final static String REQUEST_PARAMETER_NAME = "request";
    public final static String ANSWER_CONSUMER_NAME = "answer";

    private final ExpressionService expressionService;

    @Override
    public Expression create(Object... args) {
        return context -> {
            expressionService.resolve(Expressions.GET_TOKEN_FOR_ORDER_ACTION).interpret(context);
            expressionService.resolve(Expressions.PARSE_TOKEN).interpret(context);
            expressionService.resolve(Expressions.GET_CURRENT_SCOPE).interpret(context);
            expressionService.resolve(Expressions.CHANGE_SCOPE).interpret(context);
            Answer<OrderActionResponse> answer = (Answer<OrderActionResponse>) context.get(ANSWER_CONSUMER_NAME);
            answer.ans(new OrderActionResponse().putMessageItem("done", "all"));
        };
    }
}
