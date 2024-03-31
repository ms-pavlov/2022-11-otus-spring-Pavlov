package ru.otus.expressions.main;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.expressions.services.ExpressionService;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;

@ExpressionsComponent(
        expression = Expressions.PROCESS_ORDER_MESSAGE,
        scopePackages = ScopePackages.DEFAULT,
        description = "Выполнить действие над заказом")
@AllArgsConstructor
public class OrderActionProcessorExpression implements ExpressionFactory {

    private final ExpressionService expressionService;

    @Override
    public Expression create(Object... args) {
        return context -> expressionService.resolve(Expressions.DO_EXPRESSIONS,
                        expressionService.resolve(Expressions.PARSE_TOKEN),
                        expressionService.resolve(Expressions.GET_CURRENT_SCOPE),
                        expressionService.resolve(Expressions.CHANGE_SCOPE),
                        expressionService.resolve(Expressions.RESOLVE_EXPRESSIONS),
                        expressionService.resolve(Expressions.DO_ACTION))
                .interpret(context);
    }
}
