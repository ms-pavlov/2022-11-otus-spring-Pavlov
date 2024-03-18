package ru.otus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.requests.GenresRequest;
import ru.otus.dto.responses.GenresResponse;
import ru.otus.entities.Genre;
import ru.otus.mappers.GenresMapper;
import ru.otus.repositories.GenresRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GenresServiceImpl implements GenresService {

    private final GenresRepository repository;
    private final GenresMapper mapper;

    @Autowired
    public GenresServiceImpl(GenresRepository repository, GenresMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional //findByName - без транзакции, save - открывает транзакцию по-умолчанию
    public GenresResponse create(GenresRequest request) {
        if (repository.findByName(request.getName()) != null) {
            throw new GenreExistException();
        }
        return Optional.of(request)
                .map(mapper::create)
                .map(repository::save)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)  // findById - без транзакции
    public GenresResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional //findByName, findById - без транзакции, save - открывает транзакцию по-умолчанию
    public GenresResponse update(Long id, GenresRequest request) {
        Optional.ofNullable(request)
                .map(GenresRequest::getName)
                .map(repository::findByName)
                .map(Genre::getId)
                .ifPresent(value -> {
                    if (!value.equals(id)) {
                        throw new GenreExistException();
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
    @Transactional(readOnly = true) //findByName - без транзакции
    public GenresResponse findByName(String name) {
        return Optional.ofNullable(name)
                .map(repository::findByName)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true) //findAll - без транзакции
    public List<GenresResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
