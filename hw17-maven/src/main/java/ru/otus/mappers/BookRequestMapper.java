package ru.otus.mappers;

import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.entities.Book;

public interface BookRequestMapper {
    Book create(BooksRequest request);

    void update(Book entity, BooksRequest request);

    BooksResponse toDto(Book entity);
}
