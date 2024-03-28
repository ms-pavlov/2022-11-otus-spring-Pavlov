package ru.otus.securities;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.mappers.ScopeMapper;
import ru.otus.openapi.model.ScopeRequest;
import ru.otus.openapi.model.ScopeResponse;
import ru.otus.repositories.ScopeRepository;

@Service
@AllArgsConstructor
public class ScopeServiceImpl implements ScopeService {

    private final ScopeRepository scopeRepository;
    private final ScopeMapper scopeMapper;

    @Override
    public Mono<ScopeResponse> createScopes(Mono<ScopeRequest> scopeRequest) {
        return scopeRequest
                .map(scopeMapper::create)
                .flatMap(scopeRepository::save)
                .map(scopeMapper::toDto);
    }

    @Override
    public Flux<ScopeResponse> getAll() {
        return scopeRepository.findAll()
                .map(scopeMapper::toDto);
    }
}
