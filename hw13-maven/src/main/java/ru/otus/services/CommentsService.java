package ru.otus.services;

import org.springframework.security.core.Authentication;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.CommentsResponse;

import java.util.List;

public interface CommentsService {

    List<CommentsResponse> getAll();

    CommentsResponse create(
            CommentsRequest request,
            Authentication authentication);

    void delete(Long id);

}
