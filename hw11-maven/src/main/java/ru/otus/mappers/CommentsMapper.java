package ru.otus.mappers;

import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.entities.Comment;

public interface CommentsMapper {
    Comment create(CommentsRequest request);

    CommentsResponse toDto(Comment entity);
}
