package ru.otus.expressions;

@FunctionalInterface
public interface Expression {

    void interpret(ExpressionContext context);
}
