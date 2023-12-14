package ru.otus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.dto.requests.AuthorsRequest;
import ru.otus.dto.responses.AuthorsResponse;
import ru.otus.entities.Author;
import ru.otus.mappers.AuthorsMapper;
import ru.otus.repositories.AuthorsRepository;
import ru.otus.repositories.BooksRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class AuthorsServiceImpl implements AuthorsService {

    private final AuthorsRepository repository;
    private final BooksRepository booksRepository;
    private final AuthorsMapper mapper;

    @Autowired
    public AuthorsServiceImpl(
            AuthorsRepository repository,
            BooksRepository booksRepository,
            AuthorsMapper mapper) {
        this.repository = repository;
        this.booksRepository = booksRepository;
        this.mapper = mapper;
    }

    @Override
    public AuthorsResponse create(AuthorsRequest request) {
        if (repository.findByName(request.getName()) != null) {
            throw new AuthorExistException();
        }
        return Optional.of(request)
                .map(mapper::create)
                .map(repository::save)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public AuthorsResponse findById(String id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public AuthorsResponse update(String id, AuthorsRequest request) {
        Optional.ofNullable(request)
                .map(AuthorsRequest::getName)
                .map(repository::findByName)
                .map(Author::getId)
                .ifPresent(value -> {
                    if (!value.equals(id)) {
                        throw new AuthorExistException();
                    }
                });
        return repository.findById(id)
                .map(author -> {
                    mapper.update(author, request);
                    return author;
                })
                .map(repository::save)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public void delete(String id) {
        if (repository.findById(id)
                .map(booksRepository::existsByAuthorsContains)
                .orElse(false)) {
            throw new RuntimeException("Для данного автора существует книга");
        }
        repository.deleteById(id);
    }

    @Override
    public AuthorsResponse findByName(String name) {
        return Optional.ofNullable(name)
                .map(repository::findByName)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public List<AuthorsResponse> findAll() {
        return Optional.ofNullable(repository)
                .map(AuthorsRepository::findAll)
                .map(Iterable::spliterator)
                .map(authorSpliterator -> StreamSupport.stream(authorSpliterator, false))
                .orElseGet(Stream::empty)
                .map(mapper::toDto)
                .toList();
    }
}
