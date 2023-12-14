package ru.otus.controllers;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dto.requests.AuthorsRequest;
import ru.otus.dto.responses.AuthorsResponse;
import ru.otus.entities.Author;
import ru.otus.services.AuthorExistException;
import ru.otus.services.AuthorsService;

import java.io.PrintStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("Controller для работы с авторами должен")
@SpringBootTest(classes = {AuthorController.class})
class AuthorControllerTest {

    private static final String AUTHORS_ID = "1L";
    private static final String AUTHORS_NAME = "name";
    private final static Author AUTHOR = new Author(
            AUTHORS_ID,
            AUTHORS_NAME);
    private final static AuthorsResponse AUTHORS_RESPONSE = new AuthorsResponse(
            AUTHORS_ID,
            AUTHORS_NAME);
    private final static List<AuthorsResponse> AUTHORS_RESPONSE_LIST = List.of(AUTHORS_RESPONSE);

    @MockBean
    private AuthorsService service;
    @MockBean
    private PrintStream out;

    @Autowired
    private AuthorController controller;

    @Test
    @DisplayName("должен создать автора и вывести результат")
    void create() {
        doAnswer(invocationOnMock -> {
            AuthorsRequest request = invocationOnMock.getArgument(0);
            return new AuthorsResponse(
                    AUTHORS_ID,
                    request.getName());
        }).when(service).create(any());

        controller.create(AUTHOR.getName());

        verify(service, times(1)).create(any());
        verify(out, times(1)).println(eq(AUTHORS_RESPONSE.toString()));
    }

    @Test
    @DisplayName("должен проверить, что такой автор создан, и вывести сообщение ")
    void createExistAuthor() {
        when(service.create(any())).thenThrow(new AuthorExistException());

        controller.create(AUTHOR.getName());

        verify(service, times(1)).create(any());
        verify(out, times(1)).printf("Author %s already exist%n", AUTHOR.getName());
    }

    @Test
    @DisplayName("должен найти автора и вывести результат")
    void findById() {
        when(service.findById(eq(AUTHORS_ID))).thenReturn(AUTHORS_RESPONSE);

        controller.findById(AUTHORS_ID);

        verify(out, times(1)).println(AUTHORS_RESPONSE);
    }

    @Test
    @DisplayName("должен найти автора по имени и вывести результат")
    void findByName() {
        when(service.findByName(eq(AUTHORS_NAME))).thenReturn(AUTHORS_RESPONSE);

        controller.findByName(AUTHOR.getName());

        verify(out, times(1)).println(AUTHORS_RESPONSE);
    }

    @Test
    @DisplayName("должен обновить данные об авторе и вывести результат")
    void update() {
        doAnswer(invocationOnMock -> {
            AuthorsRequest request = invocationOnMock.getArgument(1);
            return new AuthorsResponse(
                    invocationOnMock.getArgument(0),
                    request.getName());
        }).when(service).update(eq(AUTHORS_ID), any());

        controller.update(AUTHORS_ID, AUTHOR.getName());

        verify(service, times(1)).update(eq(AUTHORS_ID), any());
        verify(out, times(1)).println(eq(AUTHORS_RESPONSE.toString()));
    }

    @Test
    @DisplayName("должен проверить, что такой автор создан, и вывести сообщение ")
    void updateExistAuthor() {
        when(service.update(any(), any())).thenThrow(new AuthorExistException());

        controller.update(AUTHORS_ID, AUTHOR.getName());

        verify(service, times(1)).update(any(), any());
        verify(out, times(1)).printf("Author %s already exist%n", AUTHOR.getName());
    }

    @Test
    @DisplayName("должен удалить данные об авторе")
    void delete() {
        controller.delete(AUTHORS_ID);

        verify(service, times(1)).delete(AUTHORS_ID);
    }

    @Test
    @DisplayName("должен найти всех авторов и вывести результат")
    void findAll() {
        when(service.findAll()).thenReturn(AUTHORS_RESPONSE_LIST);

        controller.findAll();

        AUTHORS_RESPONSE_LIST.forEach(
                authorsRequest -> verify(out, times(1)).println(authorsRequest));
    }
}