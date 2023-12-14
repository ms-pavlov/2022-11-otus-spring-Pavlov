package ru.otus.services;

import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.BookWithCommentsResponse;
import ru.otus.dto.responses.CommentsResponse;

import java.util.List;

public interface CommentsService {
    CommentsResponse create(CommentsRequest request);

    CommentsResponse findById(String id);

    CommentsResponse update(String id, CommentsRequest request);

    void delete(String id);

    BookWithCommentsResponse findByBookId(String bookId);

    List<CommentsResponse> findAll();
}
