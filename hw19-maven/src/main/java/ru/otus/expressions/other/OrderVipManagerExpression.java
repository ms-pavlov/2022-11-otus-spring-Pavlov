package ru.otus.expressions.other;

import lombok.AllArgsConstructor;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.entities.Order;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;
import ru.otus.securities.services.UsersService;

import static ru.otus.expressions.other.OrderParserExpression.ORDER_PARAMETER;

@ExpressionsComponent(
        expression = Expressions.SET_ORDER_MANAGER,
        scopePackages = {ScopePackages.CREATE_ORDER_BY_VIP_USER},
        description = "Создать заказ от имени простого пользователя")
@AllArgsConstructor
public class OrderVipManagerExpression implements ExpressionFactory {

    private final UsersService usersService;

    @Override
    public Expression create(Object... args) {
        return context -> {
            Order order = (Order) context.get(ORDER_PARAMETER);
            order.setManager(usersService.getUser("manager"));
        };
    }
}
