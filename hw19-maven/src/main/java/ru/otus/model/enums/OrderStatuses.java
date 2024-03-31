package ru.otus.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatuses {

    CREATED("Новый"),
    AT_WORK("Взят в работу");

    private final String name;


}
