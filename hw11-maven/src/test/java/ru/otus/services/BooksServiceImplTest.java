package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.entities.Book;
import ru.otus.mappers.BookRequestMapper;
import ru.otus.repositories.BooksRepository;
import ru.otus.repositories.CommentsRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@DisplayName("Service для работы с книгами должен")
@SpringBootTest(classes = {
        BooksServiceImpl.class
})
class BooksServiceImplTest {
    private static final String BOOKS_ID = "1L";
    private static final String BOOKS_NAME = "books_name";
    private final static Book BOOK = new Book(
            BOOKS_ID,
            BOOKS_NAME,
            List.of( "author"),
            List.of( "genre"));
    private final static BooksResponse BOOKS_RESPONSE = new BooksResponse(
            BOOK.getId(),
            BOOK.getName(),
            BOOK.getAuthors(),
            BOOK.getGenres());
    private final static BooksRequest BOOKS_REQUEST = new BooksRequest(BOOK.getName(), List.of("author"), List.of("genre"));

    @MockBean
    private BooksRepository booksRepository;
    @MockBean
    private CommentsRepository commentsRepository;
    @MockBean
    private BookRequestMapper mapper;
    @Autowired
    private BooksService service;

    @Test
    @DisplayName("должен смапить запрос в новую книгу, сохранить и вернуть результат")
    void create() {
        when(mapper.create(BOOKS_REQUEST)).thenReturn(BOOK);
        when(booksRepository.save(BOOK))
                .thenReturn(Mono.just(BOOK));
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.create(Mono.just(BOOKS_REQUEST)).block();

        assertEquals(BOOKS_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти книгу по id и вернуть результат")
    void findById() {
        when(booksRepository.findById(BOOKS_ID)).thenReturn(Mono.just(BOOK));
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.findById(BOOKS_ID).block();

        assertEquals(BOOKS_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти книгу по id, установить новые значения из запроса, обновить и вернуть результат")
    void update() {
        when(booksRepository.findById(BOOKS_ID))
                .thenReturn(Mono.just(BOOK));
        doNothing().when(mapper).update(BOOK, BOOKS_REQUEST);
        when(booksRepository.save(BOOK))
                .thenReturn(Mono.just(BOOK));
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.update(BOOKS_ID, Mono.just(BOOKS_REQUEST)).block();

        assertEquals(BOOKS_RESPONSE, result);
        verify(mapper, times(1)).update(BOOK, BOOKS_REQUEST);
        verify(booksRepository, times(1)).save(BOOK);
    }

    @Test
    @DisplayName("должен удалить книгу по id")
    void delete() {
        when(commentsRepository.deleteByBookId(BOOKS_ID)).thenReturn(Mono.empty());
        when(booksRepository.deleteById(BOOKS_ID)).thenReturn(Mono.empty());

        service.delete(BOOKS_ID).block();

        verify(booksRepository, times(1)).deleteById(BOOKS_ID);
        verify(commentsRepository, times(1)).deleteByBookId(BOOKS_ID);
    }

    @Test
    @DisplayName("должен найти постранично книги")
    void findPage() {
        when(booksRepository.findAllBy(any(Pageable.class))).thenReturn(Flux.just(BOOK));
        when(booksRepository.count()).thenReturn(Mono.just(1L));
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.findPage(Mono.just(PageRequest.of(0, 1))).block();

        assertNotNull(result);
        result.forEach(item -> assertEquals(BOOKS_RESPONSE, item));
    }
}