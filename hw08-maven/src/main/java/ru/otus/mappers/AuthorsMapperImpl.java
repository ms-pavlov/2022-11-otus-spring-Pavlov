package ru.otus.mappers;

import org.springframework.stereotype.Component;
import ru.otus.dto.requests.AuthorsRequest;
import ru.otus.dto.responses.AuthorsResponse;
import ru.otus.entities.Author;

@Component
public class AuthorsMapperImpl implements AuthorsMapper {

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
                entity.getName()
        );
    }

}
