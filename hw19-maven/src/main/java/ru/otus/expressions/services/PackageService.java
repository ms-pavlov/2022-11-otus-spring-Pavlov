package ru.otus.expressions.services;

import reactor.core.publisher.Flux;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.enums.Expressions;
import ru.otus.openapi.model.PackagesGroupResponse;

import java.util.Map;

public interface PackageService {

    void put(ExpressionFactory expressionFactory);

    Flux<PackagesGroupResponse> getPackages();

    Map<Expressions, ExpressionFactory> getPackageExpressions(String packageName);
}
