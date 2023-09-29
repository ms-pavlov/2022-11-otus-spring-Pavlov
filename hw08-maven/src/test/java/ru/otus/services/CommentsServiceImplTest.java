package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.BookWithCommentsResponse;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.entities.Book;
import ru.otus.entities.Comment;
import ru.otus.mappers.BookRequestMapper;
import ru.otus.mappers.CommentsMapper;
import ru.otus.repositories.BooksRepository;
import ru.otus.repositories.CommentsRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Service для работы с жанрами должен")
@SpringBootTest(classes = {
        CommentsServiceImpl.class
})
class CommentsServiceImplTest {

    private final static String TEST_COMMENT_ID = "1L";
    private final static String TEST_BOOK_ID = "1L";
    private final static String TEST_COMMENT = "TEST_COMMENT";
    private final static String TEST_BOOK_NAME = "books_name";
    private final static CommentsRequest TEST_COMMENT_REQUEST = new CommentsRequest(TEST_COMMENT, TEST_BOOK_ID);
    private final static CommentsResponse COMMENT_RESPONSE = new CommentsResponse(
            TEST_COMMENT_ID,
            TEST_COMMENT,
            new BooksResponse(
                    TEST_BOOK_ID,
                    TEST_BOOK_NAME,
                    List.of(),
                    List.of()));
    private final static Book TEST_BOOK = new Book(
            TEST_BOOK_ID,
            TEST_BOOK_NAME,
            List.of(),
            List.of(),
            List.of(new Comment(TEST_COMMENT_ID, TEST_COMMENT, null)));
    private final static Comment TEST_COMMENT_OBJECT = new Comment(TEST_COMMENT_ID, TEST_COMMENT, TEST_BOOK);
    private final static BookWithCommentsResponse BOOK_WITH_COMMENTS_RESPONSE = new BookWithCommentsResponse(
            new BooksResponse(
                    TEST_BOOK_ID,
                    TEST_BOOK_NAME,
                    List.of(),
                    List.of()),
            List.of(COMMENT_RESPONSE)
    );

    @MockBean
    private CommentsRepository commentsRepository;
    @MockBean
    private BooksRepository booksRepository;
    @MockBean
    private CommentsMapper commentsMapper;
    @MockBean
    private BookRequestMapper bookRequestMapper;
    @Autowired
    private CommentsService service;

    @Test
    @DisplayName("создает комментарий и возвращает результат")
    void create() {
        when(commentsMapper.create(TEST_COMMENT_REQUEST)).thenReturn(TEST_COMMENT_OBJECT);
        when(commentsRepository.save(TEST_COMMENT_OBJECT))
                .thenReturn(TEST_COMMENT_OBJECT);
        when(commentsMapper.toDto(TEST_COMMENT_OBJECT))
                .thenReturn(COMMENT_RESPONSE);

        var result = service.create(TEST_COMMENT_REQUEST);

        assertEquals(COMMENT_RESPONSE, result);
        verify(commentsRepository, times(1)).save(TEST_COMMENT_OBJECT);
        verify(commentsMapper, times(1)).create(TEST_COMMENT_REQUEST);
        verify(commentsMapper, times(1)).toDto(TEST_COMMENT_OBJECT);
    }

    @Test
    @DisplayName("должен найти комментарий по id и вернуть результат")
    void findById() {
        when(commentsRepository.findById(TEST_COMMENT_ID)).thenReturn(Optional.of(TEST_COMMENT_OBJECT));
        when(commentsMapper.toDto(TEST_COMMENT_OBJECT))
                .thenReturn(COMMENT_RESPONSE);

        var result = service.findById(TEST_COMMENT_ID);

        assertEquals(COMMENT_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти комментарий по id, установить новые значения из запроса, обновить и вернуть результат")
    void update() {
        when(commentsRepository.findById(TEST_COMMENT_ID))
                .thenReturn(Optional.of(TEST_COMMENT_OBJECT));
        doNothing().when(commentsMapper).update(TEST_COMMENT_OBJECT, TEST_COMMENT_REQUEST);
        when(commentsRepository.save(TEST_COMMENT_OBJECT))
                .thenReturn(TEST_COMMENT_OBJECT);
        when(commentsMapper.toDto(TEST_COMMENT_OBJECT))
                .thenReturn(COMMENT_RESPONSE);

        var result = service.update(TEST_COMMENT_ID, TEST_COMMENT_REQUEST);

        assertEquals(COMMENT_RESPONSE, result);
        verify(commentsMapper, times(1)).update(TEST_COMMENT_OBJECT, TEST_COMMENT_REQUEST);
        verify(commentsRepository, times(1)).save(TEST_COMMENT_OBJECT);
    }

    @Test
    @DisplayName("должен удалить комментарий по id")
    void delete() {
        service.delete(TEST_COMMENT_ID);

        verify(commentsRepository, times(1)).deleteById(TEST_COMMENT_ID);
    }

    @Test
    @DisplayName("должен найти все комментарии для книги")
    void findByBookId() {
        when(booksRepository.findById(TEST_BOOK_ID)).thenReturn(Optional.of(TEST_BOOK));
        TEST_BOOK.getComments()
                .forEach(
                        comment -> when(commentsMapper.toDto(comment))
                                .thenReturn(COMMENT_RESPONSE));
        when(bookRequestMapper.toDto(TEST_BOOK))
                .thenReturn(BOOK_WITH_COMMENTS_RESPONSE.getBook());

        var result = service.findByBookId(TEST_BOOK_ID);

        assertEquals(BOOK_WITH_COMMENTS_RESPONSE.getBook(), result.getBook());
        result.getComments().forEach(item -> assertEquals(COMMENT_RESPONSE, item));
    }

    @Test
    @DisplayName("должен найти все комментарии")
    void findAll() {
        when(commentsRepository.findAll()).thenReturn(List.of(TEST_COMMENT_OBJECT));
        when(commentsMapper.toDto(TEST_COMMENT_OBJECT))
                .thenReturn(COMMENT_RESPONSE);

        var result = service.findAll();

        result.forEach(item -> assertEquals(COMMENT_RESPONSE, item));
    }
}