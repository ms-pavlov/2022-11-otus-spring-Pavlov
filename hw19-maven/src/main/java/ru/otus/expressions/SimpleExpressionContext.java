package ru.otus.expressions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleExpressionContext implements ExpressionContext {

    private final Map<String, Object> params;

    public SimpleExpressionContext() {
        params = new ConcurrentHashMap<>();
    }

    @Override
    public void add(String name, Object object) {
        params.put(name, object);
    }

    @Override
    public Object get(String name) {
        return params.get(name);
    }
}
