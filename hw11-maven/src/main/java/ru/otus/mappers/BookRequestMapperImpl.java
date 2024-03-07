package ru.otus.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;
import ru.otus.repositories.AuthorsRepository;
import ru.otus.repositories.GenresRepository;

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
        entity.setName(request.getName());
        var authors = Optional.of(entity)
                .map(Book::getAuthors)
                .orElseGet(ArrayList::new);
        authors.clear();
        authors.addAll(request.getAuthors()
                .stream()
                .map(this::prepareAuthors)
                .toList());
        var genres = Optional.of(entity)
                .map(Book::getGenres)
                .orElseGet(ArrayList::new);
        genres.clear();
        genres.addAll(request.getGenres()
                .stream()
                .map(this::prepareGenres)
                .toList());
    }

    @Override
    public BooksResponse toDto(Book entity) {
        return new BooksResponse(
                entity.getId(),
                entity.getName(),
                Optional.of(entity)
                        .map(Book::getAuthors)
                        .orElseGet(List::of)
                        .stream()
                        .map(Author::getName)
                        .toList(),
                Optional.of(entity)
                        .map(Book::getGenres)
                        .orElseGet(List::of)
                        .stream()
                        .map(Genre::getName)
                        .toList()
        );
    }

    private Author prepareAuthors(String name) {
        return authorsRepository.findByName(name).blockOptional()
                .orElseGet(() -> authorsRepository.save(new Author(null, name)).block());
    }

    private Genre prepareGenres(String name) {
        return genresRepository.findByName(name).blockOptional()
                .orElseGet(() -> genresRepository.save(new Genre(null, name)).block());
    }
}
