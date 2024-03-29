package ru.otus.expressions.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.mappers.OrderActionMapper;
import ru.otus.openapi.model.ActionRequest;
import ru.otus.openapi.model.ActionResponse;
import ru.otus.repositories.OrderActionRepository;

@Service
@AllArgsConstructor
public class ActionServiceImpl implements ActionService {

    private final OrderActionMapper actionMapper;
    private final OrderActionRepository actionRepository;

    @Override
    public Mono<ActionResponse> addAction(ActionRequest actionRequest) {
        return actionRepository.save(actionMapper.create(actionRequest))
                .map(actionMapper::toDto);
    }

    @Override
    public Flux<ActionResponse> getAll() {
        return actionRepository.findAll()
                .map(actionMapper::toDto);
    }
}
