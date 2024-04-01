package ru.otus.expressions.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.mappers.OrderActionMapper;
import ru.otus.model.entities.OrderAction;
import ru.otus.model.enums.Expressions;
import ru.otus.openapi.model.ActionRequest;
import ru.otus.openapi.model.ActionResponse;
import ru.otus.repositories.OrderActionRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ActionServiceImpl implements ActionService {

    private final OrderActionMapper actionMapper;
    private final OrderActionRepository actionRepository;

    @Override
    public ActionResponse addAction(ActionRequest actionRequest) {
        return actionMapper.toDto(
                actionRepository.save(actionMapper.create(actionRequest)));
    }

    @Override
    public List<ActionResponse> getAll() {
        return actionRepository.findAll()
                .stream()
                .map(actionMapper::toDto)
                .toList();
    }

    @Override
    public Expressions getActionExpressionsById(Long actionId) {
        return actionRepository.findByActionId(actionId)
                .map(OrderAction::getExpression)
                .orElse(null);
    }
}
