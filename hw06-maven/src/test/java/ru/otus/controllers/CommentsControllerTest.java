package ru.otus.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.BookWithCommentsResponse;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.services.CommentsService;

import java.io.PrintStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("Mapper для работы с комментариями должен")
@SpringBootTest
class CommentsControllerTest {
    private final static Long TEST_COMMENT_ID = 1L;
    private final static Long TEST_BOOK_ID = 1L;
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
    private final static BookWithCommentsResponse BOOK_WITH_COMMENTS_RESPONSE = new BookWithCommentsResponse(
            new BooksResponse(
                    TEST_BOOK_ID,
                    TEST_BOOK_NAME,
                    List.of(),
                    List.of()),
            List.of(COMMENT_RESPONSE)
    );
    private final static BookWithCommentsResponse BOOK_WITHOUT_COMMENTS_RESPONSE = new BookWithCommentsResponse(
            new BooksResponse(
                    TEST_BOOK_ID,
                    TEST_BOOK_NAME,
                    List.of(),
                    List.of()),
            List.of()
    );

    @MockBean
    private CommentsService service;
    @MockBean
    private PrintStream out;

    @Autowired
    private CommentsController controller;
    @Test
    @DisplayName("должен комментарий и вывести результат")
    void create() {
        doAnswer(invocationOnMock -> {
            CommentsRequest request = invocationOnMock.getArgument(0);
            return new CommentsResponse(
                    TEST_COMMENT_ID,
                    request.getComment(),
                    new BooksResponse(
                            request.getBookId(),
                            TEST_BOOK_NAME,
                            List.of(),
                            List.of()));
        }).when(service).create(any());

        controller.create(TEST_COMMENT_REQUEST.getComment(), TEST_COMMENT_REQUEST.getBookId());

        verify(service, times(1)).create(any());
        verify(out, times(1)).println(eq(COMMENT_RESPONSE.toString()));
    }

    @Test
    @DisplayName("должен найти комментарий и вывести результат")
    void findById() {
        when(service.findById(eq(TEST_COMMENT_ID))).thenReturn(COMMENT_RESPONSE);

        controller.findById(TEST_COMMENT_ID);

        verify(out, times(1)).println(COMMENT_RESPONSE);
    }

    @Test
    @DisplayName("должен найти комментарий для книги и вывести результат")
    void findByBook() {
        when(service.findByBookId(eq(TEST_BOOK_ID))).thenReturn(BOOK_WITH_COMMENTS_RESPONSE);

        controller.findByBook(TEST_BOOK_ID);

        verify(out, times(1)).println(COMMENT_RESPONSE);
    }

    @Test
    @DisplayName("Если у книги нет комментариев должен вывести сообщение об этом")
    void findByBookNoComments() {
        when(service.findByBookId(eq(TEST_BOOK_ID))).thenReturn(BOOK_WITHOUT_COMMENTS_RESPONSE);

        controller.findByBook(TEST_BOOK_ID);

        verify(out, times(1)).println("No Comments");
        verify(out, times(1)).println(any(String.class));
    }

    @Test
    @DisplayName("должен обновить комментарий и вывести результат")
    void update() {
        doAnswer(invocationOnMock -> {
            CommentsRequest request = invocationOnMock.getArgument(1);
            return new CommentsResponse(
                    invocationOnMock.getArgument(0),
                    request.getComment(),
                    new BooksResponse(
                            request.getBookId(),
                            TEST_BOOK_NAME,
                            List.of(),
                            List.of()));
        }).when(service).update(eq(TEST_COMMENT_ID), any());

        controller.update(TEST_COMMENT_ID, TEST_COMMENT_REQUEST.getComment(), TEST_COMMENT_REQUEST.getBookId() );

        verify(service, times(1)).update(eq(TEST_COMMENT_ID), any());
        verify(out, times(1)).println(eq(COMMENT_RESPONSE.toString()));
    }

    @Test
    @DisplayName("должен удалить комментарий")
    void delete() {
        controller.delete(TEST_COMMENT_ID);

        verify(service, times(1)).delete(TEST_COMMENT_ID);
    }

    @Test
    @DisplayName("должен найти все комментарии и вывести результат")
    void findAll() {
        when(service.findAll()).thenReturn(List.of(COMMENT_RESPONSE));

        controller.findAll();

        verify(out, times(1)).println(COMMENT_RESPONSE.getBook());
        List.of(COMMENT_RESPONSE).forEach(
                commentsResponse -> verify(out, times(1)).println(commentsResponse));
    }
}