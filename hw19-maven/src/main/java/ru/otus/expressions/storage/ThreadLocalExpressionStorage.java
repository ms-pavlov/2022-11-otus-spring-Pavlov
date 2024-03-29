package ru.otus.expressions.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.enums.Expressions;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ThreadLocalExpressionStorage implements ExpressionStorage {

    public final static String DEFAULT_SCOPE = "main";

    private final Map<String, Map<Expressions, ExpressionFactory>> storage;
    private final StorageInitPlugin scopeInit;
    private final ThreadLocal<String> scope;

    @Autowired
    public ThreadLocalExpressionStorage(StorageInitPlugin scopeInit) {
        this.storage = new ConcurrentHashMap<>();
        this.scope = ThreadLocal.withInitial(() -> DEFAULT_SCOPE);
        this.storage.put(DEFAULT_SCOPE, new ConcurrentHashMap<>());
        this.scopeInit = scopeInit;
        scopeInit.execute(this);
    }

    @Override
    public void setScope(String scope) {
        this.scope.set(scope);
        if (!storage.containsKey(scope)) {
            storage.put(scope, new ConcurrentHashMap<>());
            scopeInit.execute(this);
        }
    }

    @Override
    public String getScope() {
        return this.scope.get();
    }

    @Override
    public void setDefaultScope() {
        this.scope.set(DEFAULT_SCOPE);
    }

    @Override
    public ExpressionFactory get(Expressions expression) {
        return Optional.ofNullable(storage.get(scope.get()))
                .map(value -> value.get(expression))
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public void put(Expressions expression, ExpressionFactory method) {
        Optional.ofNullable(storage)
                .map(value -> value.get(scope.get()))
                .map(value -> {
                    value.put(expression, method);
                    return value;
                })
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public void put(Map<Expressions, ExpressionFactory> expressionFactories) {
        Optional.ofNullable(storage)
                .map(value -> value.get(scope.get()))
                .map(value -> {
                    value.putAll(expressionFactories);
                    return value;
                })
                .orElseThrow(RuntimeException::new);
    }
}
