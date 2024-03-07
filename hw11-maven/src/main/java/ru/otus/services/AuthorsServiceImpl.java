package ru.otus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.otus.dto.responses.AuthorsResponse;
import ru.otus.mappers.AuthorsMapper;
import ru.otus.repositories.AuthorsRepository;

@Service
public class AuthorsServiceImpl implements AuthorsService {

    private final AuthorsRepository repository;
    private final AuthorsMapper mapper;

    @Autowired
    public AuthorsServiceImpl(
            AuthorsRepository repository,
            AuthorsMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<AuthorsResponse> findById(Mono<String> id) {
        return repository.findById(id)
                .map(mapper::toDto);
    }
}
