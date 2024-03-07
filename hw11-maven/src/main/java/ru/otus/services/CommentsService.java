package ru.otus.services;

import reactor.core.publisher.Mono;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.CommentsResponse;

public interface CommentsService {
    Mono<CommentsResponse> create(Mono<CommentsRequest> request);

    Mono<Void> delete(Mono<String> id);


}
