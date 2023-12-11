package ru.otus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.dto.requests.GenresRequest;
import ru.otus.dto.responses.GenresResponse;
import ru.otus.entities.Genre;
import ru.otus.mappers.GenresMapper;
import ru.otus.repositories.GenresRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
    public GenresResponse create(GenresRequest request) {
        if(repository.findByName(request.getName()) != null) {
            throw new GenreExistException();
        }
        return Optional.of(request)
                .map(mapper::create)
                .map(repository::save)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public GenresResponse findById(String id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public GenresResponse update(String id, GenresRequest request) {
        Optional.ofNullable(request)
                .map(GenresRequest::getName)
                .map(repository::findByName)
                .map(Genre::getId)
                .ifPresent(value -> {
                    if(!value.equals(id)) {
                        throw new GenreExistException();
                    }});

        return repository.findById(id)
                .map(genre -> {
                    mapper.update(genre, request);
                    return genre;
                })
                .map(repository::save)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public GenresResponse findByName(String name) {
        return Optional.ofNullable(name)
                .map(repository::findByName)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public List<GenresResponse> findAll() {
        return Optional.ofNullable(repository)
                .map(GenresRepository::findAll)
                .map(Iterable::spliterator)
                .map(genreSpliterator -> StreamSupport.stream(genreSpliterator, false))
                .orElseGet(Stream::empty)
                .map(mapper::toDto)
                .toList();
    }
}
