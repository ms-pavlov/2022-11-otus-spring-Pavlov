package ru.otus.expressions.other;


import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.Expression;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.entities.Order;
import ru.otus.model.entities.OrderItem;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;
import ru.otus.openapi.model.OrderActionRequest;
import ru.otus.order.OrderActionServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ExpressionsComponent(
        expression = Expressions.PARSE_ORDER,
        scopePackages = {ScopePackages.CREATE_ORDER_BY_USER, ScopePackages.CREATE_ORDER_BY_VIP_USER},
        description = "Создать заказ от имени простого пользователя")
public class OrderParserExpression implements ExpressionFactory {

    public final static String ORDER_PARAMETER = "order";

    @Override
    public Expression create(Object... args) {
        return context -> {
            OrderActionRequest action = (OrderActionRequest) context.get(OrderActionServiceImpl.REQUEST_PARAMETER_NAME);

            Order order = new Order();

            order.setOrderItems(mapArgsToOrderItems(action));
            context.add(ORDER_PARAMETER, order);
        };
    }

    private List<OrderItem> mapArgsToOrderItems(OrderActionRequest action) {
        return Optional.ofNullable(action)
                .map(OrderActionRequest::getArgs)
                .orElseGet(Map::of)
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        entry -> getIndex(entry.getKey())))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .stream()
                                .collect(Collectors.toMap(
                                        value -> getKey(value.getKey()),
                                        Map.Entry::getValue
                                ))
                ))
                .values()
                .stream()
                .map(
                        params -> {
                            OrderItem result = new OrderItem();
                            result.setProduct(params.get("product"));
                            result.setQuantity(Long.parseLong(params.get("quantity")));
                            return result;
                        }
                )
                .toList();
    }

    private Long getIndex(String key) {
        Pattern pattern = Pattern.compile("\\Q[\\E(.*?)\\Q]\\E");
        Matcher matcher = pattern.matcher(key);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        throw new RuntimeException("Недопустимый параметр: " + key);
    }

    private String getKey(String key) {
        Pattern pattern = Pattern.compile("\\.(\\S+)");
        Matcher matcher = pattern.matcher(key);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
