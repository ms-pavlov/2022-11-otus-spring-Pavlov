package ru.otus.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;
import ru.otus.repositories.AuthorsRepository;
import ru.otus.repositories.GenresRepository;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BookRequestMapperImpl implements BookRequestMapper {

    private final AuthorsRepository authorsRepository;
    private final GenresRepository genresRepository;

    @Autowired
    public BookRequestMapperImpl(AuthorsRepository authorsRepository, GenresRepository genresRepository) {
        this.authorsRepository = authorsRepository;
        this.genresRepository = genresRepository;
    }

    @Override
    public Book create(BooksRequest request) {
        var result = new Book();
        update(result, request);
        return result;
    }

    @Override
    public void update(Book entity, BooksRequest request) {
        entity.setName(request.name());
        entity.setAuthors(request.authors()
                .stream()
                .map(name -> prepareAuthors(name, entity))
                .toList());
        entity.setGenres(request.genres()
                .stream()
                .map(this::prepareGenres)
                .toList());

    }

    @Override
    public BooksResponse toDto(Book entity) {
        return new BooksResponse(entity);
    }

    private Author prepareAuthors(String name, Book book) {
        return Optional.ofNullable(authorsRepository.findByName(name))
                .orElseGet(() -> authorsRepository.create(new Author(null, name, List.of(book))));
    }

    private Genre prepareGenres(String name) {
        return Optional.ofNullable(genresRepository.findByName(name))
                .orElseGet(() -> genresRepository.create(new Genre(null, name)));
    }
}
