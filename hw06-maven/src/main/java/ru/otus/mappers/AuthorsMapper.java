package ru.otus.mappers;

import ru.otus.dto.requests.AuthorsRequest;
import ru.otus.dto.responses.AuthorsResponse;
import ru.otus.entities.Author;

public interface AuthorsMapper {

    Author create(AuthorsRequest request);

    void update(Author entity, AuthorsRequest request);

    AuthorsResponse toDto(Author entity);
}
