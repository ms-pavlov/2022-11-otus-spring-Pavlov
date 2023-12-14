package ru.otus.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dto.requests.GenresRequest;
import ru.otus.dto.responses.GenresResponse;
import ru.otus.entities.Genre;
import ru.otus.services.GenreExistException;
import ru.otus.services.GenresService;

import java.io.PrintStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("Controller для работы с жанрами должен")
@SpringBootTest(classes = {GenresController.class})
class GenresControllerTest {

    private static final String GENRES_ID = "1L";
    private static final String GENRES_NAME = "name";
    private final static Genre GENRE = new Genre(
            GENRES_ID,
            GENRES_NAME);
    private final static GenresResponse GENRES_RESPONSE = new GenresResponse(
            GENRES_ID,
            GENRES_NAME);

    private final static List<GenresResponse> GENRES_RESPONSE_LIST = List.of(GENRES_RESPONSE);

    @MockBean
    private GenresService service;
    @MockBean
    private PrintStream out;

    @Autowired
    private GenresController controller;

    @Test
    @DisplayName("должен создать автора и вывести результат")
    void create() {
        doAnswer(invocationOnMock -> {
            GenresRequest request = invocationOnMock.getArgument(0);
            return new GenresResponse(
                    GENRES_ID,
                    request.getName());
        }).when(service).create(any());

        controller.create(GENRE.getName());

        verify(service, times(1)).create(any());
        verify(out, times(1)).println(eq(GENRES_RESPONSE.toString()));
    }

    @Test
    @DisplayName("должен проверить, что такой жанр создан, и вывести сообщение ")
    void createExistGenre() {
        when(service.create(any())).thenThrow(new GenreExistException());

        controller.create(GENRE.getName());

        verify(service, times(1)).create(any());
        verify(out, times(1)).printf("Genre %s already exist%n", GENRE.getName());
    }

    @Test
    @DisplayName("должен найти жанр и вывести результат")
    void findById() {
        when(service.findById(eq(GENRES_ID))).thenReturn(GENRES_RESPONSE);

        controller.findById(GENRES_ID);

        verify(out, times(1)).println(GENRES_RESPONSE);
    }

    @Test
    @DisplayName("должен найти жанр по названию и вывести результат")
    void findByName() {
        when(service.findByName(eq(GENRES_NAME))).thenReturn(GENRES_RESPONSE);

        controller.findByName(GENRE.getName());

        verify(out, times(1)).println(GENRES_RESPONSE);
    }

    @Test
    @DisplayName("должен обновить данные об жанре и вывести результат")
    void update() {
        doAnswer(invocationOnMock -> {
            GenresRequest request = invocationOnMock.getArgument(1);
            return new GenresResponse(
                    invocationOnMock.getArgument(0),
                    request.getName());
        }).when(service).update(eq(GENRES_ID), any());

        controller.update(GENRES_ID, GENRE.getName());

        verify(service, times(1)).update(eq(GENRES_ID), any());
        verify(out, times(1)).println(eq(GENRES_RESPONSE.toString()));
    }

    @Test
    @DisplayName("должен проверить, что такой жанр создан, и вывести сообщение ")
    void updateExistGenre() {
        when(service.update(any(), any())).thenThrow(new GenreExistException());

        controller.update(GENRES_ID, GENRE.getName());

        verify(service, times(1)).update(any(), any());
        verify(out, times(1)).printf("Genre %s already exist%n", GENRE.getName());
    }

    @Test
    @DisplayName("должен удалить данные о жаре")
    void delete() {
        controller.delete(GENRES_ID);

        verify(service, times(1)).delete(GENRES_ID);
    }

    @Test
    @DisplayName("должен найти все жанры и вывести результат")
    void findAll() {
        when(service.findAll()).thenReturn(GENRES_RESPONSE_LIST);

        controller.findAll();

        GENRES_RESPONSE_LIST.forEach(
                genresResponse -> verify(out, times(1)).println(genresResponse));
    }
}