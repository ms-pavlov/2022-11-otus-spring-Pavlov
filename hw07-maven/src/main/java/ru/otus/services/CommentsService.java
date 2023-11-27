package ru.otus.services;

import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.BookWithCommentsResponse;
import ru.otus.dto.responses.CommentsResponse;

import java.util.List;

public interface CommentsService {
    CommentsResponse create(CommentsRequest request);

    CommentsResponse findById(Long id);

    CommentsResponse update(Long id, CommentsRequest request);

    void delete(Long id);

    BookWithCommentsResponse findByBookId(Long bookId);

    List<CommentsResponse> findAll();
}
