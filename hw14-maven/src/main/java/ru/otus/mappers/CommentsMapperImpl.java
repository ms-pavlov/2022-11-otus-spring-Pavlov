package ru.otus.mappers;

import org.springframework.stereotype.Component;
import ru.otus.mongo.entities.Comment;
import ru.otus.postgre.entities.Book;
import ru.otus.storages.BookCashStorage;
import ru.otus.storages.CashStorage;

import java.util.Optional;

@Component
public class CommentsMapperImpl implements CommentsMapper {

    private final CashStorage<ru.otus.mongo.entities.Book, Long> bookCashStorage;

    public CommentsMapperImpl(BookCashStorage bookCashStorage) {
        this.bookCashStorage = bookCashStorage;
    }

    @Override
    public Comment create(ru.otus.postgre.entities.Comment request) {
        Comment result = new Comment();
        update(result, request);
        return result;
    }

    private void update(Comment entity, ru.otus.postgre.entities.Comment comment) {
        entity.setComment(comment.getComment());
        Optional.of(comment)
                .map(ru.otus.postgre.entities.Comment::getBook)
                .map(Book::getId)
                .map(bookCashStorage::get)
                .ifPresent(entity::setBook);
    }

}
