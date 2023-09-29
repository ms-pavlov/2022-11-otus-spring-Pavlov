package ru.otus.mappers;

import org.springframework.stereotype.Component;
import ru.otus.dto.requests.GenresRequest;
import ru.otus.dto.responses.GenresResponse;
import ru.otus.entities.Genre;

@Component
public class GenresMapperImpl implements GenresMapper {

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
                entity.getName()
        );
    }
}
