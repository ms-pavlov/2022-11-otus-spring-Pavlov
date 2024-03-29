package ru.otus.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Expressions {

    PROCESS_ORDER_MESSAGE("OrderAction::process", "Выполнить действие над заказом"),
    CREATE_ORDER("Order::create", "Создать заказ");


    private final static Map<String, Expressions> EXPRESSIONS = Arrays.stream(Expressions.values())
            .collect(Collectors.toMap(
                    Expressions::getExpressionName,
                    expressions -> expressions));

    private final String expressionName;
    private final String expressionDescription;

    public static Expressions getByName(String name) {
        return EXPRESSIONS.get(name);
    }
}
