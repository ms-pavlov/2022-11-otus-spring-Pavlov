package ru.otus.expressions.services;

import ru.otus.expressions.Expression;
import ru.otus.model.enums.Expressions;

public interface ExpressionService {

    Expression resolve(Expressions expression, Object... args);
}
