package ru.otus.expressions.services;

import ru.otus.model.enums.Expressions;
import ru.otus.openapi.model.ActionRequest;
import ru.otus.openapi.model.ActionResponse;

import java.util.List;

public interface ActionService {

    ActionResponse addAction(ActionRequest actionRequest);

    List<ActionResponse> getAll();

    Expressions getActionExpressionsById(Long actionId);
}
