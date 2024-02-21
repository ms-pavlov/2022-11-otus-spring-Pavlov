package ru.otus.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.*;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;
import ru.otus.repositories.AuthorsRepository;
import ru.otus.repositories.GenresRepository;

import java.util.ArrayList;
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
                entity.getAuthors()
                        .stream()
                        .map(author -> new AuthorsShortResponse(author.getId(), author.getName()))
                        .toList(),
                entity.getGenres()
                        .stream()
                        .map(genre -> new GenresShortResponse(genre.getId(), genre.getName()))
                        .toList(),
                null
        );
    }

    @Override
    public BooksResponse toFullDto(Book entity) {
        return new BooksResponse(
                entity.getId(),
                entity.getName(),
                entity.getAuthors()
                        .stream()
                        .map(author -> new AuthorsShortResponse(author.getId(), author.getName()))
                        .toList(),
                entity.getGenres()
                        .stream()
                        .map(genre -> new GenresShortResponse(genre.getId(), genre.getName()))
                        .toList(),
                entity.getComments()
                        .stream()
                        .map(comment -> new CommentsResponse(comment.getId(), comment.getComment()))
                        .toList()
        );
    }

    private Author prepareAuthors(String name) {
        return Optional.ofNullable(authorsRepository.findByName(name))
                .orElseGet(() -> authorsRepository.save(new Author(null, name)));
    }

    private Genre prepareGenres(String name) {
        return Optional.ofNullable(genresRepository.findByName(name))
                .orElseGet(() -> genresRepository.save(new Genre(null, name)));
    }
}
