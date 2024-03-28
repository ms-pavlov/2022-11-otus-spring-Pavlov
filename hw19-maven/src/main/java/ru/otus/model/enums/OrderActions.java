package ru.otus.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum OrderActions {

    CREATE(1L, Expressions.CREATE_ORDER);

    private final static Map<Long, Expressions> EXPRESSIONS = Arrays.stream(OrderActions.values())
            .collect(Collectors.toMap(
                    OrderActions::getId,
                    OrderActions::getExpression
            ));

    private final Long id;
    private final Expressions expression;

    public static Expressions getById(Long id) {
        return EXPRESSIONS.get(id);
    }
}
