package ru.otus.order;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.otus.expressions.SimpleExpressionContext;
import ru.otus.expressions.services.ExpressionService;
import ru.otus.model.enums.Expressions;
import ru.otus.openapi.model.OrderAction;
import ru.otus.openapi.model.OrderActionResponse;

import static ru.otus.expressions.main.OrderActionProcessorExpression.ANSWER_CONSUMER_NAME;
import static ru.otus.expressions.main.OrderActionProcessorExpression.REQUEST_PARAMETER_NAME;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ExpressionService expressionService;

    @Override
    public Mono<OrderActionResponse> processMessages(OrderAction orderAction) {
        return Mono.create(
                sink -> {
                    var context = new SimpleExpressionContext();
                    context.add(REQUEST_PARAMETER_NAME, orderAction);
                    context.add(ANSWER_CONSUMER_NAME, (Answer<OrderActionResponse>) sink::success);

                    expressionService.resolve(Expressions.PROCESS_ORDER_MESSAGE)
                            .interpret(context);
                }
        );
    }
}
