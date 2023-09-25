package ru.otus.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.entities.Comment;

import java.util.Optional;

@Component
public class CommentsMapperImpl implements CommentsMapper {

    private final BookRequestMapper bookMapper;

    @Autowired
    public CommentsMapperImpl(BookRequestMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public Comment create(CommentsRequest request) {
        Comment result = new Comment();
        update(result, request);
        return result;
    }

    @Override
    public void update(Comment entity, CommentsRequest request) {
        entity.setComment(request.getComment());
    }

    @Override
    public CommentsResponse toDto(Comment entity) {
        return new CommentsResponse(
                entity.getId(),
                entity.getComment(),
                Optional.of(entity)
                        .map(Comment::getBook)
                        .map(bookMapper::toDto)
                        .orElse(null));
    }
}
