package ru.otus.expressions.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.mappers.ScopeMapper;
import ru.otus.model.entities.Scope;
import ru.otus.openapi.model.ScopeRequest;
import ru.otus.openapi.model.ScopeResponse;
import ru.otus.repositories.ScopeRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ScopeServiceImpl implements ScopeService {

    private final ScopeRepository scopeRepository;
    private final ScopeMapper scopeMapper;

    @Override
    public ScopeResponse createScopes(ScopeRequest scopeRequest) {
        return scopeMapper.toDto(
                scopeRepository.save(
                        scopeMapper.create(scopeRequest)));
    }

    @Override
    public List<ScopeResponse> getAll() {
        return scopeRepository.findAll()
                .stream()
                .map(scopeMapper::toDto)
                .toList();
    }

    @Override
    public Scope getByName(String scopeName) {
        return scopeRepository.findByName(scopeName);
    }
}
