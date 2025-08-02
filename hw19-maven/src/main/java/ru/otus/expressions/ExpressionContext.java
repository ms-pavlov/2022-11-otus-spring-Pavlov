package ru.otus.expressions;

public interface ExpressionContext {

    void add(String name, Object object);

    Object get(String name);
}
