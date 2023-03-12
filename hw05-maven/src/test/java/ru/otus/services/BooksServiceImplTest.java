package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dao.BooksDao;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.entities.Authors;
import ru.otus.entities.Books;
import ru.otus.entities.Genres;
import ru.otus.mappers.BookRequestMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Service для работы с книгами должен")
@SpringBootTest
class BooksServiceImplTest {
    private static final Long BOOKS_ID = 1L;
    private static final String BOOKS_NAME = "books_name";
    private static final String AUTHORS_NAME = "AUTHORS_NAME";
    private static final String GENRES_NAME = "GENRES_NAME";
    private final static Books BOOK = new Books(
            1L,
            "name",
            List.of(new Authors(1L, "author")),
            List.of(new Genres(1L, "genre")));
    private final static BooksResponse BOOKS_RESPONSE = new BooksResponse(BOOK);
    private final static BooksRequest BOOKS_REQUEST = new BooksRequest(BOOK.getName(), List.of("author"), List.of("genre"));

    @MockBean
    private BooksDao booksDao;
    @MockBean
    private BookRequestMapper mapper;
    @Autowired
    private BooksService service;

    @Test
    @DisplayName("должен смапить запрос в новую книгу, сохранить и вернуть результат")
    void create() {
        when(mapper.create(BOOKS_REQUEST)).thenReturn(BOOK);
        when(booksDao.create(BOOK))
                .thenReturn(BOOK);
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.create(BOOKS_REQUEST);

        assertEquals(BOOKS_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти книгу по id и вернуть результат")
    void findById() {
        when(booksDao.getById(BOOKS_ID)).thenReturn(Optional.of(BOOK));
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.findById(BOOKS_ID);

        assertEquals(BOOKS_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти книгу по id, установить новые значения из запроса, обновить и вернуть результат")
    void update() {
        when(booksDao.getById(BOOKS_ID))
                .thenReturn(Optional.of(BOOK));
        doNothing().when(mapper).update(BOOK, BOOKS_REQUEST);
        when(booksDao.update(BOOK))
                .thenReturn(BOOK);
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.update(BOOKS_ID, BOOKS_REQUEST);

        assertEquals(BOOKS_RESPONSE, result);
        verify(mapper, times(1)).update(BOOK, BOOKS_REQUEST);
    }

    @Test
    @DisplayName("должен удалить книгу по id")
    void delete() {
        service.delete(BOOKS_ID);

        verify(booksDao, times(1)).delete(BOOKS_ID);
    }

    @Test
    @DisplayName("должен найти книгу по названию")
    void findByName() {
        when(booksDao.getByName(BOOKS_NAME)).thenReturn(List.of(BOOK));
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.findByName(BOOKS_NAME);

        result.forEach(item -> assertEquals(BOOKS_RESPONSE, item));

    }

    @Test
    @DisplayName("должен найти книги по автору")
    void findByAuthor() {
        when(booksDao.getByAuthor(AUTHORS_NAME)).thenReturn(List.of(BOOK));
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.findByAuthor(AUTHORS_NAME);

        result.forEach(item -> assertEquals(BOOKS_RESPONSE, item));
    }

    @Test
    @DisplayName("должен найти книги по жанру")
    void findByGenres() {
        when(booksDao.getByGenre(GENRES_NAME)).thenReturn(List.of(BOOK));
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.findByGenre(GENRES_NAME);

        result.forEach(item -> assertEquals(BOOKS_RESPONSE, item));
    }

    @Test
    @DisplayName("должен найти все книги")
    void findAll() {
        when(booksDao.getAll()).thenReturn(List.of(BOOK));
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.findAll();

        result.forEach(item -> assertEquals(BOOKS_RESPONSE, item));
    }
}