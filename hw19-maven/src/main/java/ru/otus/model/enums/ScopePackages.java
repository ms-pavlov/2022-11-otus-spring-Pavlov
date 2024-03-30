package ru.otus.model.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum ScopePackages {

    DEFAULT("default", ScopePackagesGroups.DEFAULT, "Действия по умолчанию"),
    CREATE_ORDER_BY_USER("createOrderByUser", ScopePackagesGroups.CREATE_ORDER, "Создание заказа пользователем по умолчанию"),
    CREATE_ORDER_BY_VIP_USER("createOrderByVipUser", ScopePackagesGroups.CREATE_ORDER, "Создание заказа VIP-пользователем по умолчанию"),
    CREATE_ORDER_BY_MANAGER("createOrderByManager", ScopePackagesGroups.CREATE_ORDER, "Создание заказа менеджером по умолчанию");

    private final static List<String> PACKAGE_NAMES = Arrays.stream(ScopePackages.values())
            .map(ScopePackages::getPackageName)
            .toList();

    private final static Map<String, ScopePackages> PACKAGES = Arrays.stream(ScopePackages.values())
            .collect(Collectors.toMap(
                    ScopePackages::getPackageName,
                    scopePackages -> scopePackages));

    private final String packageName;
    private final ScopePackagesGroups packageGroup;
    private final String packageDescription;

    public static boolean contains(String packageName) {
        return PACKAGE_NAMES.contains(packageName);
    }

    public static ScopePackages getByName(String name) {
        return PACKAGES.get(name);
    }
}
