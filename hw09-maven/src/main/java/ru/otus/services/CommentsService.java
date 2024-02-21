package ru.otus.services;

import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.CommentsResponse;

public interface CommentsService {

    CommentsResponse create(CommentsRequest request);

    void delete(Long id);

}
