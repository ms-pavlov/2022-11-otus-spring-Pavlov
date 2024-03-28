package ru.otus.securities;

import ru.otus.expressions.Expression;

public interface ExpressionService {

    Expression resolve(Expression expression, String scope, Object... args);
}
