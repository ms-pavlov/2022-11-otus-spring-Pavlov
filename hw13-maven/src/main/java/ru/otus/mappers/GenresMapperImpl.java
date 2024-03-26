package ru.otus.mappers;

import org.springframework.stereotype.Component;
import ru.otus.dto.requests.GenresRequest;
import ru.otus.dto.responses.GenresResponse;
import ru.otus.entities.Genre;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class GenresMapperImpl implements GenresMapper {

    private final BookRequestMapper bookMapper;

    public GenresMapperImpl(BookRequestMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public Genre create(GenresRequest request) {
        var result = new Genre();
        update(result, request);
        return result;
    }

    @Override
    public void update(Genre entity, GenresRequest request) {
        entity.setName(request.getName());
    }

    @Override
    public GenresResponse toDto(Genre entity) {
        return new GenresResponse(
                entity.getId(),
                entity.getName(),
                Optional.of(entity)
                        .map(Genre::getBooks)
                        .orElseGet(ArrayList::new)
                        .stream()
                        .map(bookMapper::toDto)
                        .toList()
        );
    }
}
