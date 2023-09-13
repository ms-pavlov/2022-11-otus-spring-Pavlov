package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;
import ru.otus.mappers.BookRequestMapper;
import ru.otus.repositories.BooksRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Service для работы с книгами должен")
@SpringBootTest(classes = {
        BooksServiceImpl.class
})
class BooksServiceImplTest {
    private static final Long BOOKS_ID = 1L;
    private static final String BOOKS_NAME = "books_name";
    private final static Book BOOK = new Book(
            1L,
            "name",
            List.of(new Author(1L, "author")),
            List.of(new Genre(1L, "genre")));
    private final static BooksResponse BOOKS_RESPONSE = new BooksResponse(BOOK);
    private final static BooksRequest BOOKS_REQUEST = new BooksRequest(BOOK.getName(), List.of("author"), List.of("genre"));

    @MockBean
    private BooksRepository booksRepository;
    @MockBean
    private BookRequestMapper mapper;
    @Autowired
    private BooksService service;

    @Test
    @DisplayName("должен смапить запрос в новую книгу, сохранить и вернуть результат")
    void create() {
        when(mapper.create(BOOKS_REQUEST)).thenReturn(BOOK);
        when(booksRepository.create(BOOK))
                .thenReturn(BOOK);
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.create(BOOKS_REQUEST);

        assertEquals(BOOKS_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти книгу по id и вернуть результат")
    void findById() {
        when(booksRepository.getById(BOOKS_ID)).thenReturn(Optional.of(BOOK));
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.findById(BOOKS_ID);

        assertEquals(BOOKS_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти книгу по id, установить новые значения из запроса, обновить и вернуть результат")
    void update() {
        when(booksRepository.getById(BOOKS_ID))
                .thenReturn(Optional.of(BOOK));
        doNothing().when(mapper).update(BOOK, BOOKS_REQUEST);
        when(booksRepository.update(BOOK))
                .thenReturn(BOOK);
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.update(BOOKS_ID, BOOKS_REQUEST);

        assertEquals(BOOKS_RESPONSE, result);
        verify(mapper, times(1)).update(BOOK, BOOKS_REQUEST);
        verify(booksRepository, times(1)).update(BOOK);
    }

    @Test
    @DisplayName("должен удалить книгу по id")
    void delete() {
        service.delete(BOOKS_ID);

        verify(booksRepository, times(1)).delete(BOOKS_ID);
    }

    @Test
    @DisplayName("должен найти книгу по названию")
    void findByName() {
        when(booksRepository.getByName(BOOKS_NAME)).thenReturn(List.of(BOOK));
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.findByName(BOOKS_NAME);

        result.forEach(item -> assertEquals(BOOKS_RESPONSE, item));

    }

    @Test
    @DisplayName("должен найти все книги")
    void findAll() {
        when(booksRepository.getAll()).thenReturn(List.of(BOOK));
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.findAll();

        result.forEach(item -> assertEquals(BOOKS_RESPONSE, item));
    }
}