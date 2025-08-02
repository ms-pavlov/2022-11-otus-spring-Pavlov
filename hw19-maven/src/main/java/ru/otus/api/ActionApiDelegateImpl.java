package ru.otus.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.otus.expressions.services.ActionService;
import ru.otus.openapi.api.ActionApiDelegate;
import ru.otus.openapi.model.ActionRequest;
import ru.otus.openapi.model.ActionResponse;

import java.util.List;

@Service
@AllArgsConstructor
public class ActionApiDelegateImpl implements ActionApiDelegate {

    private final ActionService actionService;

    @Override
    public ResponseEntity<ActionResponse> addAction(ActionRequest actionRequest) {
        return ResponseEntity.ok(actionService.addAction(actionRequest));
    }

    @Override
    public ResponseEntity<List<ActionResponse>> getAll() {
        return ResponseEntity.ok(actionService.getAll());
    }
}
