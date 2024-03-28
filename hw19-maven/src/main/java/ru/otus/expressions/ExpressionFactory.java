package ru.otus.expressions;

public interface ExpressionFactory {

    Expression create(Object... args);
}
