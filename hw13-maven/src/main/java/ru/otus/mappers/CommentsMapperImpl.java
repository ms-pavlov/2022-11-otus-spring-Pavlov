package ru.otus.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.entities.Comment;
import ru.otus.entities.User;
import ru.otus.repositories.BooksRepository;
import ru.otus.repositories.UsersRepository;

import java.util.Optional;

@Component
public class CommentsMapperImpl implements CommentsMapper {

    private final BooksRepository booksRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public CommentsMapperImpl(
            BooksRepository booksRepository,
            UsersRepository usersRepository) {
        this.booksRepository = booksRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public Comment create(
            CommentsRequest request,
            Authentication authentication) {
        Comment result = new Comment();
        update(result, request);
        result.setOwner(
                Optional.ofNullable(authentication)
                        .map(Authentication::getName)
                        .map(usersRepository::findByLogin)
                        .orElse(null));
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
                entity.getComment(),
                Optional.of(entity)
                        .map(Comment::getOwner)
                        .map(User::getName)
                        .orElse("Аноним"),
                Optional.of(entity)
                        .map(Comment::getOwner)
                        .map(User::getLogin)
                        .orElse(null));
    }
}
