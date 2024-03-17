package ru.otus.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.mongo.entities.Author;
import ru.otus.mongo.entities.Book;
import ru.otus.mongo.entities.Genre;
import ru.otus.mongo.repositories.AuthorsMongoRepository;
import ru.otus.mongo.repositories.GenresMongoRepository;

import java.util.Optional;

@Component
public class BookRequestMapperImpl implements BookRequestMapper {

    private final AuthorsMongoRepository authorsMongoRepository;
    private final GenresMongoRepository genresMongoRepository;

    @Autowired
    public BookRequestMapperImpl(AuthorsMongoRepository authorsMongoRepository, GenresMongoRepository genresMongoRepository) {
        this.authorsMongoRepository = authorsMongoRepository;
        this.genresMongoRepository = genresMongoRepository;
    }

    @Override
    public Book create(ru.otus.postgre.entities.Book request) {
        var result = new Book();
        update(result, request);
        return result;
    }

    private void update(Book entity, ru.otus.postgre.entities.Book request) {
        entity.setName(request.getName());
        entity.setAuthors(
                request.getAuthors()
                        .stream()
                        .map(ru.otus.postgre.entities.Author::getName)
                        .map(this::prepareAuthors)
                        .toList());
        entity.setGenres(
                request.getGenres()
                        .stream()
                        .map(ru.otus.postgre.entities.Genre::getName)
                        .map(this::prepareGenres)
                        .toList());
    }

    private Author prepareAuthors(String name) {
        return Optional.ofNullable(authorsMongoRepository.findByName(name))
                .orElseThrow(() -> new RuntimeException("Неизвестный автор"));
    }

    private Genre prepareGenres(String name) {
        return Optional.ofNullable(genresMongoRepository.findByName(name))
                .orElseThrow(() -> new RuntimeException("Неизвестный жанр"));
    }
}
