package ru.otus.controllers;

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
import ru.otus.services.BooksService;

import java.io.PrintStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Controller для работы с книгами должен")
@SpringBootTest(classes = {BooksController.class})
class BooksControllerTest {
    private final static long BOOK_ID = 1L;
    private static final String BOOKS_NAME = "books_name";
    private static final String AUTHORS_NAME = "AUTHORS_NAME";
    private static final String GENRES_NAME = "GENRES_NAME";
    private final static Book BOOK = new Book(
            1L,
            BOOKS_NAME,
            List.of(new Author(1L, AUTHORS_NAME)),
            List.of(new Genre(1L, GENRES_NAME)));
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
    private final static List<BooksResponse> BOOKS_RESPONSE_LIST = List.of(BOOKS_RESPONSE);

    @MockBean
    private BooksService service;
    @MockBean
    private PrintStream out;

    @Autowired
    private BooksController controller;


    @Test
    @DisplayName("должен создать книгу и вывести результат")
    void create() {
        doAnswer(invocationOnMock -> {
            BooksRequest request = invocationOnMock.getArgument(0);
            return new BooksResponse(
                    BOOK_ID,
                    request.getName(),
                    request.getAuthors(),
                    request.getGenres());
        }).when(service).create(any());

        controller.create(BOOK.getName(), List.of(AUTHORS_NAME), List.of(GENRES_NAME));

        verify(service, times(1)).create(any());
        verify(out, times(1)).println(eq(BOOKS_RESPONSE.toString()));
    }

    @Test
    @DisplayName("должен найти книгу и вывести результат")
    void findById() {
        when(service.findById(eq(BOOK_ID))).thenReturn(BOOKS_RESPONSE);

        controller.findById(BOOK_ID);

        verify(out, times(1)).println(BOOKS_RESPONSE);
    }

    @Test
    @DisplayName("должен найти книги по названию и вывести результат")
    void findByName() {
        when(service.findByName(eq(BOOKS_NAME))).thenReturn(BOOKS_RESPONSE_LIST);

        controller.findByName(BOOK.getName());

        BOOKS_RESPONSE_LIST.forEach(
                booksResponse -> verify(out, times(1)).println(booksResponse));
    }

    @Test
    @DisplayName("должен найти все книги и вывести результат")
    void findAll() {
        when(service.findAll()).thenReturn(BOOKS_RESPONSE_LIST);

        controller.findAll();

        BOOKS_RESPONSE_LIST.forEach(
                booksResponse -> verify(out, times(1)).println(booksResponse));
    }

    @Test
    @DisplayName("должен обновить данные о книге и вывести результат")
    void update() {
        doAnswer(invocationOnMock -> {
            BooksRequest request = invocationOnMock.getArgument(1);
            return new BooksResponse(
                    invocationOnMock.getArgument(0),
                    request.getName(),
                    request.getAuthors(),
                    request.getGenres());
        }).when(service).update(eq(BOOK_ID), any());

        controller.update(BOOK_ID, BOOKS_NAME, List.of(AUTHORS_NAME), List.of(GENRES_NAME));

        verify(out, times(1)).println(eq(BOOKS_RESPONSE.toString()));
    }

    @Test
    @DisplayName("должен удалить данные о книге")
    void delete() {
        controller.delete(BOOK_ID);

        verify(service, times(1)).delete(BOOK_ID);
    }
}