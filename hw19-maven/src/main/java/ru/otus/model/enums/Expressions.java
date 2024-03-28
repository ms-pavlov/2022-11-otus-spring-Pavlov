package ru.otus.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Expressions {

    CREATE_ORDER("Order::create", "Создать заказ");

    private final String expressionName;
    private final String expressionDescription;
}
