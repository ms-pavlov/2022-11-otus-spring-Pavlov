package ru.otus.mappers;

import ru.otus.dto.requests.GenresRequest;
import ru.otus.dto.responses.GenresResponse;
import ru.otus.entities.Genre;

public interface GenresMapper {

    Genre create(GenresRequest request);

    void update(Genre entity, GenresRequest request);

    GenresResponse toDto(Genre entity);
}
