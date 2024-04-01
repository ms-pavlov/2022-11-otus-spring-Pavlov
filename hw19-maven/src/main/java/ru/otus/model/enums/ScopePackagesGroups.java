package ru.otus.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScopePackagesGroups {

    DEFAULT("default", "Базовый покет"),
    GET_ORDERS("getAllOrder", "Получение списка заказов"),
    CREATE_ORDER("createOrder", "Создание заказа"),
    ORDER_STATUSES("orderStatuses", "Смена статуса заказа");


    private final String groupName;
    private final String groupDescription;
}
