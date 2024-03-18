package ru.otus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.requests.AuthorsRequest;
import ru.otus.dto.responses.AuthorsResponse;
import ru.otus.entities.Author;
import ru.otus.mappers.AuthorsMapper;
import ru.otus.repositories.AuthorsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorsServiceImpl implements AuthorsService {

    private final AuthorsRepository repository;
    private final AuthorsMapper mapper;

    @Autowired
    public AuthorsServiceImpl(AuthorsRepository repository, AuthorsMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
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
    @Transactional(readOnly = true)
    public AuthorsResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public AuthorsResponse update(Long id, AuthorsRequest request) {
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorsResponse findByName(String name) {
        return Optional.ofNullable(name)
                .map(repository::findByName)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorsResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
