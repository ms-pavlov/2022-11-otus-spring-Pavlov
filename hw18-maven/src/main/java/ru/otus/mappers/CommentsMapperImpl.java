package ru.otus.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.entities.Comment;
import ru.otus.repositories.BooksRepository;

import java.util.Optional;

@Component
public class CommentsMapperImpl implements CommentsMapper {

    private final BooksRepository booksRepository;

    @Autowired
    public CommentsMapperImpl(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
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
        Optional.of(request)
                .map(CommentsRequest::getBookId)
                .flatMap(booksRepository::findById)
                .ifPresent(entity::setBook);
    }

    @Override
    public CommentsResponse toDto(Comment entity) {
        return new CommentsResponse(
                entity.getId(),
                entity.getComment());
    }
}
