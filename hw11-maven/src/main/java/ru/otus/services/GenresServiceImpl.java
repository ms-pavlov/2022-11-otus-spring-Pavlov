package ru.otus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.otus.dto.responses.GenresResponse;
import ru.otus.mappers.GenresMapper;
import ru.otus.repositories.GenresRepository;

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
    public Mono<GenresResponse> findById(Mono<String> id) {
        return repository.findById(id)
                .map(mapper::toDto);
    }

}
