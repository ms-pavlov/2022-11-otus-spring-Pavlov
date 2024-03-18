package ru.otus.mappers;

import org.springframework.security.core.Authentication;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.entities.Comment;

public interface CommentsMapper {
    Comment create(
            CommentsRequest request,
            Authentication authentication);

    void update(Comment entity, CommentsRequest request);

    CommentsResponse toDto(Comment entity);
}
