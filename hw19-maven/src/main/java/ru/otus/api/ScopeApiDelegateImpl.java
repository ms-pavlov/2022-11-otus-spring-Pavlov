package ru.otus.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.otus.expressions.services.ScopeService;
import ru.otus.openapi.api.ScopeApiDelegate;
import ru.otus.openapi.model.ScopeRequest;
import ru.otus.openapi.model.ScopeResponse;

import java.util.List;

@Service
@AllArgsConstructor
public class ScopeApiDelegateImpl implements ScopeApiDelegate {

    private final ScopeService scopeService;

    @Override
    public ResponseEntity<ScopeResponse> createScopes(ScopeRequest scopeRequest) {
        return ResponseEntity.ok(scopeService.createScopes(scopeRequest));
    }

    @Override
    public ResponseEntity<List<ScopeResponse>> getScopes() {
        return ResponseEntity.ok(scopeService.getAll());
    }
}
