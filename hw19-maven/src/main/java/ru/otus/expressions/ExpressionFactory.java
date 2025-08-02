package ru.otus.expressions;

@FunctionalInterface
public interface ExpressionFactory {

    Expression create(Object... args);
}
