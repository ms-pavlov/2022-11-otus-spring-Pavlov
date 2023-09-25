package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.entities.Book;
import ru.otus.entities.Comment;
import ru.otus.mappers.CommentsMapper;
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
    private final static Book TEST_BOOK = new Book(
            TEST_BOOK_ID,
            TEST_BOOK_NAME,
            List.of(),
            List.of(),
            List.of());
    private final static Comment TEST_COMMENT_OBJECT = new Comment(TEST_COMMENT_ID, TEST_COMMENT, TEST_BOOK);

    @MockBean
    private CommentsRepository repository;
    @MockBean
    private CommentsMapper mapper;
    @Autowired
    private CommentsService service;

    @Test
    @DisplayName("создает комментарий и возвращает результат")
    void create() {
        when(mapper.create(TEST_COMMENT_REQUEST)).thenReturn(TEST_COMMENT_OBJECT);
        when(repository.create(TEST_COMMENT_OBJECT))
                .thenReturn(TEST_COMMENT_OBJECT);
        when(mapper.toDto(TEST_COMMENT_OBJECT))
                .thenReturn(COMMENT_RESPONSE);

        var result = service.create(TEST_COMMENT_REQUEST);

        assertEquals(COMMENT_RESPONSE, result);
        verify(repository, times(1)).create(TEST_COMMENT_OBJECT);
        verify(mapper, times(1)).create(TEST_COMMENT_REQUEST);
        verify(mapper, times(1)).toDto(TEST_COMMENT_OBJECT);
    }

    @Test
    @DisplayName("должен найти комментарий по id и вернуть результат")
    void findById() {
        when(repository.getById(TEST_COMMENT_ID)).thenReturn(Optional.of(TEST_COMMENT_OBJECT));
        when(mapper.toDto(TEST_COMMENT_OBJECT))
                .thenReturn(COMMENT_RESPONSE);

        var result = service.findById(TEST_COMMENT_ID);

        assertEquals(COMMENT_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти комментарий по id, установить новые значения из запроса, обновить и вернуть результат")
    void update() {
        when(repository.getById(TEST_COMMENT_ID))
                .thenReturn(Optional.of(TEST_COMMENT_OBJECT));
        doNothing().when(mapper).update(TEST_COMMENT_OBJECT, TEST_COMMENT_REQUEST);
        when(repository.update(TEST_COMMENT_OBJECT))
                .thenReturn(TEST_COMMENT_OBJECT);
        when(mapper.toDto(TEST_COMMENT_OBJECT))
                .thenReturn(COMMENT_RESPONSE);

        var result = service.update(TEST_COMMENT_ID, TEST_COMMENT_REQUEST);

        assertEquals(COMMENT_RESPONSE, result);
        verify(mapper, times(1)).update(TEST_COMMENT_OBJECT, TEST_COMMENT_REQUEST);
        verify(repository, times(1)).update(TEST_COMMENT_OBJECT);
    }

    @Test
    @DisplayName("должен удалить комментарий по id")
    void delete() {
        service.delete(TEST_COMMENT_ID);

        verify(repository, times(1)).delete(TEST_COMMENT_ID);
    }

    @Test
    @DisplayName("должен найти все комментарии для книги")
    void findByBookId() {
        when(repository.getCommentsByBookId(TEST_BOOK_ID)).thenReturn(List.of(TEST_COMMENT_OBJECT));
        when(mapper.toDto(TEST_COMMENT_OBJECT))
                .thenReturn(COMMENT_RESPONSE);

        var result = service.findAll();

        result.forEach(item -> assertEquals(COMMENT_RESPONSE, item));
    }

    @Test
    @DisplayName("должен найти все комментарии")
    void findAll() {
        when(repository.getAll()).thenReturn(List.of(TEST_COMMENT_OBJECT));
        when(mapper.toDto(TEST_COMMENT_OBJECT))
                .thenReturn(COMMENT_RESPONSE);

        var result = service.findAll();

        result.forEach(item -> assertEquals(COMMENT_RESPONSE, item));
    }
}