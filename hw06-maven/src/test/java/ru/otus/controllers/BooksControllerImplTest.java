package ru.otus.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.controllers.BooksController;
import ru.otus.controllers.BooksControllerImpl;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;
import ru.otus.services.BooksService;

import java.io.PrintStream;
import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("Controller для работы с книгами должен")
@SpringBootTest(classes = {BooksControllerImpl.class})
class BooksControllerImplTest {
    private final static long BOOK_ID = 1L;
    private static final String BOOKS_NAME = "books_name";
    private static final String AUTHORS_NAME = "AUTHORS_NAME";
    private static final String GENRES_NAME = "GENRES_NAME";
    private final static Book BOOK = new Book(
            1L,
            BOOKS_NAME,
            List.of(new Author(1L, AUTHORS_NAME, List.of())),
            List.of(new Genre(1L, GENRES_NAME)));
    private final static BooksResponse BOOKS_RESPONSE = new BooksResponse(BOOK);
    private final static List<BooksResponse> BOOKS_RESPONSE_LIST = List.of(BOOKS_RESPONSE);
    private final static BooksRequest BOOKS_REQUEST = new BooksRequest(BOOK.getName(), List.of(AUTHORS_NAME), List.of(GENRES_NAME));

    @MockBean
    private BooksService service;
    @MockBean
    private PrintStream out;

    @Autowired
    private BooksController controller;


    @Test
    @DisplayName("должен создать книгу и вывести результат")
    void create() {
        when(service.create(eq(BOOKS_REQUEST))).thenReturn(BOOKS_RESPONSE);

        controller.create(BOOK.getName(), List.of(AUTHORS_NAME), List.of(GENRES_NAME));

        verify(out, times(1)).println(BOOKS_RESPONSE);
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
    @DisplayName("должен найти книги по автору и вывести результат")
    void findByAuthor() {
        when(service.findByGenre(eq(GENRES_NAME))).thenReturn(BOOKS_RESPONSE_LIST);

        controller.findByGenre(GENRES_NAME);

        BOOKS_RESPONSE_LIST.forEach(
                booksResponse -> verify(out, times(1)).println(booksResponse));
    }

    @Test
    @DisplayName("должен найти книги по жанру и вывести результат")
    void findByGenre() {
        when(service.findByAuthor(eq(AUTHORS_NAME))).thenReturn(BOOKS_RESPONSE_LIST);

        controller.findByAuthor(AUTHORS_NAME);

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
        when(service.update(eq(BOOK_ID), eq(BOOKS_REQUEST))).thenReturn(BOOKS_RESPONSE);

        controller.update(BOOK_ID, BOOKS_NAME, List.of(AUTHORS_NAME), List.of(GENRES_NAME));

        verify(out, times(1)).println(BOOKS_RESPONSE);
    }

    @Test
    @DisplayName("должен удалить данные о книге")
    void delete() {
        controller.delete(BOOK_ID);

        verify(service, times(1)).delete(BOOK_ID);
    }
}