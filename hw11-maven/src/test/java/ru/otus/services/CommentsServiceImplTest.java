package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.entities.Book;
import ru.otus.entities.Comment;
import ru.otus.mappers.CommentsMapper;
import ru.otus.repositories.CommentsRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@DisplayName("Service для работы с комментариями должен")
@SpringBootTest(classes = {
        CommentsServiceImpl.class
})
class CommentsServiceImplTest {

    private final static String TEST_COMMENT_ID = "1L";
    private final static String TEST_BOOK_ID = "1L";
    private final static String TEST_COMMENT = "TEST_COMMENT";
    private final static String TEST_BOOK_NAME = "books_name";
    private final static CommentsRequest TEST_COMMENT_REQUEST = new CommentsRequest(TEST_COMMENT, TEST_BOOK_ID);
    private final static Comment COMMENT = new Comment(TEST_COMMENT, TEST_BOOK_ID, null);
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
            List.of());
    private final static Comment TEST_COMMENT_OBJECT = new Comment(TEST_COMMENT_ID, TEST_COMMENT, TEST_BOOK);
    private final static Mono<Comment> TEST_COMMENT_OBJECT_MONO = Mono.just(COMMENT);
    private final static Mono<CommentsRequest> TEST_COMMENT_REQUEST_MONO = Mono.just(TEST_COMMENT_REQUEST);
    private final static Flux<Comment> TEST_COMMENT_OBJECT_FLUX = Flux.just(COMMENT);

    @MockBean
    private CommentsRepository commentsRepository;
    @MockBean
    private CommentsMapper commentsMapper;
    @Autowired
    private CommentsService service;

    @Test
    @DisplayName("создает комментарий и возвращает результат")
    void create() {
        when(commentsMapper.create(TEST_COMMENT_REQUEST)).thenReturn(TEST_COMMENT_OBJECT);
        when(commentsRepository.save(TEST_COMMENT_OBJECT))
                .thenReturn(TEST_COMMENT_OBJECT_MONO);
        when(commentsMapper.toDto(COMMENT))
                .thenReturn(COMMENT_RESPONSE);

        var result = service.create(TEST_COMMENT_REQUEST_MONO).block();

        assertEquals(COMMENT_RESPONSE, result);
        verify(commentsRepository, times(1)).save(TEST_COMMENT_OBJECT);
        verify(commentsMapper, times(1)).create(TEST_COMMENT_REQUEST);
        verify(commentsMapper, times(1)).toDto(COMMENT);
    }

    @Test
    @DisplayName("должен удалить комментарий по id")
    void delete() {
        var request = Mono.just(TEST_COMMENT_ID);
        when(commentsRepository.deleteById(request)).thenReturn(Mono.empty());

        service.delete(request).block();

        verify(commentsRepository, times(1)).deleteById(request);
    }

    @Test
    @DisplayName("должен найти все комментарии для книги")
    void findByBookId() {
        when(commentsRepository.findByBookId(TEST_BOOK_ID)).thenReturn(TEST_COMMENT_OBJECT_FLUX);
        when(commentsMapper.toDto(COMMENT))
                .thenReturn(COMMENT_RESPONSE);
        when(commentsMapper.toDto(TEST_COMMENT_OBJECT))
                .thenReturn(COMMENT_RESPONSE);

        var result = service.getCommentsByBook(Mono.just(TEST_BOOK_ID)).collectList().block();

        assertNotNull(result);
        result.forEach(item -> assertEquals(COMMENT_RESPONSE, item));
    }

}