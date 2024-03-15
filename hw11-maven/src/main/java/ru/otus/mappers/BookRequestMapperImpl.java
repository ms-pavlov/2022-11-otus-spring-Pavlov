package ru.otus.mappers;

import org.springframework.stereotype.Component;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.entities.Book;

@Component
public class BookRequestMapperImpl implements BookRequestMapper {

    @Override
    public Book create(BooksRequest request) {
        var result = new Book();
        update(result, request);
        return result;
    }

    @Override
    public void update(Book entity, BooksRequest request) {
        entity.setName(request.getName());
        entity.setAuthors(request.getAuthors());
        entity.setGenres(request.getGenres());
    }

    @Override
    public BooksResponse toDto(Book entity) {
        return new BooksResponse(
                entity.getId(),
                entity.getName(),
                entity.getAuthors(),
                entity.getGenres()
        );
    }

}
