package ru.otus.mappers;

import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.entities.Books;

public interface BookRequestMapper {
    Books create(BooksRequest request);

    void update(Books entity, BooksRequest request);

    BooksResponse toDto(Books entity);
}
