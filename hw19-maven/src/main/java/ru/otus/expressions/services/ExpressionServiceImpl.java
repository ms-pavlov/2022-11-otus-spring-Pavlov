package ru.otus.expressions.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.expressions.Expression;
import ru.otus.expressions.storage.ExpressionStorage;
import ru.otus.model.enums.Expressions;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ExpressionServiceImpl implements ExpressionService {

    private final ExpressionStorage expressionStorage;

    @Override
    public Expression resolve(Expressions expression, Object... args) {
        return Optional.ofNullable(expression)
                .map(expressionStorage::get)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Неизвестное выражение %s", expression)))
                .create(args);
    }
}
