package ru.otus.services;

import org.springframework.beans.factory.annotation.Autowired;
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
public class GenresServiceImpl implements GenresService{

    private final GenresRepository repository;
    private final GenresMapper mapper;

    @Autowired
    public GenresServiceImpl(GenresRepository repository, GenresMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public GenresResponse create(GenresRequest request) {
        if(repository.getByName(request.getName()) != null) {
            throw new GenreExistException();
        }
        return Optional.of(request)
                .map(mapper::create)
                .map(repository::create)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public GenresResponse findById(Long id) {
        return repository.getById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public GenresResponse update(Long id, GenresRequest request) {
        Optional.ofNullable(request)
                .map(GenresRequest::getName)
                .map(repository::getByName)
                .map(Genre::getId)
                .ifPresent(value -> {
                    if(!value.equals(id)) {
                        throw new GenreExistException();
                    }});

        return repository.getById(id)
                .map(author -> {
                    mapper.update(author, request);
                    return author;
                })
                .map(repository::update)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public GenresResponse findByName(String name) {
        return Optional.ofNullable(name)
                .map(repository::getByName)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenresResponse> findAll() {
        return repository.getAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
