package ru.otus.expressions.storage;

import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.enums.Expressions;

import java.util.Map;

public interface ExpressionStorage {

    void setScope(String scope);

    String getScope();

    void setDefaultScope();

    ExpressionFactory get(Expressions expression);

    void put(Expressions expression, ExpressionFactory method);

    void put(Map<Expressions, ExpressionFactory> expressionFactories);
}
