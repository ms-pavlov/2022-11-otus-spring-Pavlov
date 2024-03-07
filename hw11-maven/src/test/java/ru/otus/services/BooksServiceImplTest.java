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
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;
import ru.otus.mappers.BookRequestMapper;
import ru.otus.repositories.BooksRepository;
import ru.otus.repositories.CommentsRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
            List.of(new Author("1L", "author")),
            List.of(new Genre("1L", "genre")));
    private final static BooksResponse BOOKS_RESPONSE = new BooksResponse(
            BOOK.getId(),
            BOOK.getName(),
            BOOK.getAuthors()
                    .stream()
                    .map(Author::getName)
                    .toList(),
            BOOK.getGenres()
                    .stream()
                    .map(Genre::getName)
                    .toList());
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
                .thenReturn(BOOK);
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.create(BOOKS_REQUEST);

        assertEquals(BOOKS_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти книгу по id и вернуть результат")
    void findById() {
        when(booksRepository.findById(BOOKS_ID)).thenReturn(Optional.of(BOOK));
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.findById(BOOKS_ID);

        assertEquals(BOOKS_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти книгу по id, установить новые значения из запроса, обновить и вернуть результат")
    void update() {
        when(booksRepository.findById(BOOKS_ID))
                .thenReturn(Optional.of(BOOK));
        doNothing().when(mapper).update(BOOK, BOOKS_REQUEST);
        when(booksRepository.save(BOOK))
                .thenReturn(BOOK);
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.update(BOOKS_ID, BOOKS_REQUEST);

        assertEquals(BOOKS_RESPONSE, result);
        verify(mapper, times(1)).update(BOOK, BOOKS_REQUEST);
        verify(booksRepository, times(1)).save(BOOK);
    }

    @Test
    @DisplayName("должен удалить книгу по id")
    void delete() {
        service.delete(BOOKS_ID);

        verify(booksRepository, times(1)).deleteById(BOOKS_ID);
        verify(commentsRepository, times(1)).deleteByBookId(BOOKS_ID);
    }

    @Test
    @DisplayName("должен найти постранично книги")
    void findPage() {
        when(booksRepository.findPageable(any(Pageable.class))).thenReturn(Flux.just(BOOK));
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.findPage(Mono.just(PageRequest.of(0, 1))).block();

        result.forEach(item -> assertEquals(BOOKS_RESPONSE, item));
    }
}