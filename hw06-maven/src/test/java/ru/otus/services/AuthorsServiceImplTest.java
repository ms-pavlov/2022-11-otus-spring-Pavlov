package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dto.requests.AuthorsRequest;
import ru.otus.dto.responses.AuthorsResponse;
import ru.otus.entities.Author;
import ru.otus.mappers.AuthorsMapper;
import ru.otus.repositories.AuthorsRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Service для работы с авторами должен")
@SpringBootTest(classes = {
        AuthorsServiceImpl.class
})
class AuthorsServiceImplTest {

    private static final Long AUTHORS_ID = 1L;
    private static final String AUTHORS_NAME = "name";
    private final static Author AUTHOR = new Author(
            AUTHORS_ID,
            AUTHORS_NAME);
    private final static AuthorsResponse AUTHORS_RESPONSE = new AuthorsResponse(
            AUTHORS_ID,
            AUTHORS_NAME,
            List.of());
    private final static AuthorsRequest AUTHOR_REQUEST = new AuthorsRequest(AUTHOR.getName());


    @MockBean
    private AuthorsRepository repository;
    @MockBean
    private AuthorsMapper mapper;
    @Autowired
    private AuthorsService service;

    @Test
    @DisplayName("должен проверить что такой автор ещё не создан, сохранить и вернуть результат")
    void create() {
        when(repository.existName(AUTHORS_NAME)).thenReturn(false);
        when(mapper.create(AUTHOR_REQUEST)).thenReturn(AUTHOR);
        when(repository.create(AUTHOR))
                .thenReturn(AUTHOR);
        when(mapper.toDto(AUTHOR))
                .thenReturn(AUTHORS_RESPONSE);

        var result = service.create(AUTHOR_REQUEST);

        assertEquals(AUTHORS_RESPONSE, result);
        verify(repository, times(1)).existName(AUTHORS_NAME);
        verify(repository, times(1)).create(AUTHOR);
        verify(mapper, times(1)).create(AUTHOR_REQUEST);
        verify(mapper, times(1)).toDto(AUTHOR);
    }

    @Test
    @DisplayName("должен проверить, что такой автор создан, и вернуть исключение")
    void createExistAuthor() {
        when(repository.existName(AUTHORS_NAME)).thenReturn(true);

        assertThrows(AuthorExistException.class, () -> service.create(AUTHOR_REQUEST));
    }

    @Test
    @DisplayName("должен найти автора по id и вернуть результат")
    void findById() {
        when(repository.getById(AUTHORS_ID)).thenReturn(Optional.of(AUTHOR));
        when(mapper.toDto(AUTHOR))
                .thenReturn(AUTHORS_RESPONSE);

        var result = service.findById(AUTHORS_ID);

        assertEquals(AUTHORS_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти автора по id, установить новые значения из запроса, обновить и вернуть результат")
    void update() {
        when(repository.getById(AUTHORS_ID))
                .thenReturn(Optional.of(AUTHOR));
        doNothing().when(mapper).update(AUTHOR, AUTHOR_REQUEST);
        when(repository.update(AUTHOR))
                .thenReturn(AUTHOR);
        when(mapper.toDto(AUTHOR))
                .thenReturn(AUTHORS_RESPONSE);

        var result = service.update(AUTHORS_ID, AUTHOR_REQUEST);

        assertEquals(AUTHORS_RESPONSE, result);
        verify(mapper, times(1)).update(AUTHOR, AUTHOR_REQUEST);
        verify(repository, times(1)).update(AUTHOR);
    }

    @Test
    @DisplayName("должен проверить, что такой автор создан, и вернуть исключение")
    void updateExistAuthor() {
        when(repository.existName(AUTHORS_NAME)).thenReturn(true);

        assertThrows(AuthorExistException.class, () -> service.update(AUTHORS_ID, AUTHOR_REQUEST));
    }

    @Test
    @DisplayName("должен удалить автора по id")
    void delete() {
        service.delete(AUTHORS_ID);

        verify(repository, times(1)).delete(AUTHORS_ID);
    }

    @Test
    @DisplayName("должен найти автора по имени")
    void findByName() {
        when(repository.getByName(AUTHORS_NAME)).thenReturn(AUTHOR);
        when(mapper.toDto(AUTHOR))
                .thenReturn(AUTHORS_RESPONSE);

        var result = service.findByName(AUTHORS_NAME);

        assertEquals(AUTHORS_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти всех авторов")
    void findAll() {
        when(repository.getAll()).thenReturn(List.of(AUTHOR));
        when(mapper.toDto(AUTHOR))
                .thenReturn(AUTHORS_RESPONSE);

        var result = service.findAll();

        result.forEach(item -> assertEquals(AUTHORS_RESPONSE, item));
    }
}