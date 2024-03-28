package ru.otus.model.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum ScopePackages {

    CREATE_ORDER_BY_USER("createOrderByUser", "Создание заказа пользователем по умолчанию"),
    CREATE_ORDER_BY_VIP_USER("createOrderByVipUser", "Создание заказа VIP-пользователем по умолчанию"),
    CREATE_ORDER_BY_MANAGER("createOrderByManager", "Создание заказа менеджером по умолчанию");

    private final static List<String> PACKAGE_NAMES = Arrays.stream(ScopePackages.values())
            .map(ScopePackages::getPackageName)
            .toList();

    private final String packageName;
    private final String packageDescription;

    public static boolean contains(String packageName) {
        return PACKAGE_NAMES.contains(packageName);
    }
}
