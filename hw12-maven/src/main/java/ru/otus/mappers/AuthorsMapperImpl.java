package ru.otus.mappers;

import org.springframework.stereotype.Component;
import ru.otus.dto.requests.AuthorsRequest;
import ru.otus.dto.responses.AuthorsResponse;
import ru.otus.entities.Author;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class AuthorsMapperImpl implements AuthorsMapper {

    private final BookRequestMapper bookMapper;

    public AuthorsMapperImpl(BookRequestMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public Author create(AuthorsRequest request) {
        var result = new Author();
        update(result, request);
        return result;
    }

    @Override
    public void update(Author entity, AuthorsRequest request) {
        entity.setName(request.getName());
    }

    @Override
    public AuthorsResponse toDto(Author entity) {
        return new AuthorsResponse(
                entity.getId(),
                entity.getName(),
                Optional.of(entity)
                        .map(Author::getBooks)
                        .orElseGet(ArrayList::new)
                        .stream()
                        .map(bookMapper::toDto)
                        .toList()
        );
    }

}
