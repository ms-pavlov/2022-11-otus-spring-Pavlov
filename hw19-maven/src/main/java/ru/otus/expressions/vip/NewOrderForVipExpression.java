package ru.otus.expressions.vip;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.expressions.services.ExpressionService;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.OrderStatuses;
import ru.otus.model.enums.ScopePackages;

@ExpressionsComponent(
        expression = Expressions.CREATE_ORDER,
        scopePackages = ScopePackages.CREATE_ORDER_BY_VIP_USER,
        description = "Создать заказ от имени простого пользователя")
@AllArgsConstructor
public class NewOrderForVipExpression implements ExpressionFactory {

    private final ExpressionService expressionService;

    @Override
    public Expression create(Object... args) {
        return context -> expressionService.resolve(Expressions.DO_EXPRESSIONS,
                        expressionService.resolve(Expressions.PARSE_ORDER),
                        expressionService.resolve(Expressions.SET_ORDER_CUSTOMER),
                        expressionService.resolve(Expressions.SET_ORDER_STATUS, OrderStatuses.AT_WORK),
                        expressionService.resolve(Expressions.SET_ORDER_MANAGER),
                        expressionService.resolve(Expressions.SAVE_ORDER))
                .interpret(context);
    }
}