package ru.otus.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.dao.AuthorsDao;
import ru.otus.dao.GenresDao;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.entities.Authors;
import ru.otus.entities.Books;
import ru.otus.entities.Genres;

import java.util.Optional;

@Component
public class BookRequestMapperImpl implements BookRequestMapper {

    private final AuthorsDao authorsDao;
    private final GenresDao genresDao;

    @Autowired
    public BookRequestMapperImpl(AuthorsDao authorsDao, GenresDao genresDao) {
        this.authorsDao = authorsDao;
        this.genresDao = genresDao;
    }

    @Override
    public Books create(BooksRequest request) {
        return new Books(
                null,
                request.name(),
                request.authors()
                        .stream()
                        .map(this::prepareAuthors)
                        .toList(),
                request.genres()
                        .stream()
                        .map(this::prepareGenres)
                        .toList());
    }

    @Override
    public void update(Books entity, BooksRequest request) {
        entity.setName(request.name());
        entity.setAuthors(request.authors()
                .stream()
                .map(this::prepareAuthors)
                .toList());
        entity.setGenres(request.genres()
                .stream()
                .map(this::prepareGenres)
                .toList());

    }

    @Override
    public BooksResponse toDto(Books entity) {
        return new BooksResponse(entity);
    }

    private Authors prepareAuthors(String name) {
        return Optional.ofNullable(authorsDao.findByName(name))
                .orElseGet(() -> authorsDao.create(new Authors(null, name)));
    }

    private Genres prepareGenres(String name) {
        return Optional.ofNullable(genresDao.findByName(name))
                .orElseGet(() -> genresDao.create(new Genres(null, name)));
    }
}
