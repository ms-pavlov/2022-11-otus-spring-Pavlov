package ru.otus.model;

import lombok.Builder;

@Builder
public record MapModel<T, ID>(T object, ID id) {

}
