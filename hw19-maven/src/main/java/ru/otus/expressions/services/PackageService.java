package ru.otus.expressions.services;

import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;
import ru.otus.openapi.model.PackagesGroupResponse;

import java.util.List;
import java.util.Map;

public interface PackageService {

    void put(Expressions expression, ScopePackages scopePackages, ExpressionFactory expressionFactory);

    List<PackagesGroupResponse> getPackages();

    Map<Expressions, ExpressionFactory> getPackageExpressions(String packageName);
}
