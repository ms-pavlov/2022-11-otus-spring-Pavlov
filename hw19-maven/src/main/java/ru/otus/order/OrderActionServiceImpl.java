package ru.otus.order;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.expressions.SimpleExpressionContext;
import ru.otus.expressions.services.ExpressionService;
import ru.otus.model.enums.Expressions;
import ru.otus.openapi.model.OrderActionRequest;
import ru.otus.openapi.model.OrderActionResponse;
import ru.otus.securities.services.UsersService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderActionServiceImpl implements OrderActionService {

    public final static String REQUEST_PARAMETER_NAME = "request";
    public final static String ANSWER_CONSUMER_NAME = "answer";
    public final static String CURRENT_USER_PARAMETER_NAME = "user";

    private final ExpressionService expressionService;
    private final UsersService usersService;

    @Override
    public OrderActionResponse processMessages(OrderActionRequest orderAction) {
        List<OrderActionResponse> result = new ArrayList<>();
        var context = prepareContext(orderAction, result);
        expressionService.resolve(Expressions.PROCESS_ORDER_MESSAGE)
                .interpret(context);

        return result.stream().findAny().orElse(null);
    }

    private SimpleExpressionContext prepareContext(OrderActionRequest orderAction, List<OrderActionResponse> result) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var context = new SimpleExpressionContext();
        context.add(REQUEST_PARAMETER_NAME, orderAction);
        context.add(ANSWER_CONSUMER_NAME, (Answer<OrderActionResponse>) result::add);
        context.add(
                CURRENT_USER_PARAMETER_NAME,
                Optional.ofNullable(authentication)
                        .map(Principal::getName)
                        .map(usersService::getUser)
                        .orElse(null));
        return context;
    }
}
