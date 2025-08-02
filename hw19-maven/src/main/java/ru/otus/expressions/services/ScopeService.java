package ru.otus.expressions.services;

import ru.otus.model.entities.Scope;
import ru.otus.openapi.model.ScopeRequest;
import ru.otus.openapi.model.ScopeResponse;

import java.util.List;

public interface ScopeService {

    ScopeResponse createScopes(ScopeRequest scopeRequest);

    List<ScopeResponse> getAll();

    Scope getByName(String scopeName);
}
