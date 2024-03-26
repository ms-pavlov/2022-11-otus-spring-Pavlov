package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
class AuthorsServiceImplTest {

    private static final Long AUTHORS_ID = 1L;
    private static final String AUTHORS_NAME = "name";
    private final static Author AUTHOR = new Author(
            AUTHORS_ID,
            AUTHORS_NAME);

    private final static Author OTHERT_AUTHOR = new Author(
            100L + AUTHORS_ID,
            "other" + AUTHORS_NAME);
    private final static AuthorsResponse AUTHORS_RESPONSE = new AuthorsResponse(
            AUTHORS_ID,
            AUTHORS_NAME,
            List.of());
    private final static AuthorsRequest AUTHOR_REQUEST = new AuthorsRequest(AUTHOR.getName());


    @MockBean
    private AuthorsRepository repository;
    @MockBean
    private AuthorsMapper mapper;
    @MockBean
    private UsersService usersService;

    @Autowired
    private AuthorsService service;

    @Test
    @WithMockUser(
            roles = "ADMIN"
    )
    @DisplayName("должен проверить что такой автор ещё не создан, сохранить и вернуть результат")
    void create() {
        when(repository.findByName(AUTHORS_NAME)).thenReturn(null);
        when(mapper.create(AUTHOR_REQUEST)).thenReturn(AUTHOR);
        when(repository.save(AUTHOR))
                .thenReturn(AUTHOR);
        when(mapper.toDto(AUTHOR))
                .thenReturn(AUTHORS_RESPONSE);

        var result = service.create(AUTHOR_REQUEST);

        assertEquals(AUTHORS_RESPONSE, result);
        verify(repository, times(1)).findByName(AUTHORS_NAME);
        verify(repository, times(1)).save(AUTHOR);
        verify(mapper, times(1)).create(AUTHOR_REQUEST);
        verify(mapper, times(1)).toDto(AUTHOR);
    }

    @Test
    @WithMockUser(
            roles = "ADMIN"
    )
    @DisplayName("должен проверить, что такой автор создан, и вернуть исключение")
    void createExistAuthor() {
        when(repository.findByName(AUTHORS_NAME)).thenReturn(AUTHOR);

        assertThrows(AuthorExistException.class, () -> service.create(AUTHOR_REQUEST));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("создание не возможно для анонимного пользователя")
    void createFail() {
        assertThrows(AccessDeniedException.class, () -> service.create(AUTHOR_REQUEST));
    }

    @Test
    @DisplayName("должен найти автора по id и вернуть результат")
    void findById() {
        when(repository.findById(AUTHORS_ID)).thenReturn(Optional.of(AUTHOR));
        when(mapper.toDto(AUTHOR))
                .thenReturn(AUTHORS_RESPONSE);

        var result = service.findById(AUTHORS_ID);

        assertEquals(AUTHORS_RESPONSE, result);
    }

    @Test
    @WithMockUser(
            roles = "ADMIN"
    )
    @DisplayName("должен найти автора по id, установить новые значения из запроса, обновить и вернуть результат")
    void update() {
        when(repository.findById(AUTHORS_ID))
                .thenReturn(Optional.of(AUTHOR));
        doNothing().when(mapper).update(AUTHOR, AUTHOR_REQUEST);
        when(repository.save(AUTHOR))
                .thenReturn(AUTHOR);
        when(mapper.toDto(AUTHOR))
                .thenReturn(AUTHORS_RESPONSE);

        var result = service.update(AUTHORS_ID, AUTHOR_REQUEST);

        assertEquals(AUTHORS_RESPONSE, result);
        verify(mapper, times(1)).update(AUTHOR, AUTHOR_REQUEST);
        verify(repository, times(1)).save(AUTHOR);
    }

    @Test
    @WithMockUser(
            roles = "ADMIN"
    )
    @DisplayName("должен проверить, что такой автор создан, и вернуть исключение")
    void updateExistAuthor() {
        when(repository.findByName(AUTHORS_NAME)).thenReturn(OTHERT_AUTHOR);

        assertThrows(AuthorExistException.class, () -> service.update(AUTHORS_ID, AUTHOR_REQUEST));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("изменение не возможно для анонимного пользователя")
    void updateFail() {
        assertThrows(AccessDeniedException.class, () -> service.update(AUTHORS_ID, AUTHOR_REQUEST));
    }

    @Test
    @WithMockUser(
            roles = "ADMIN"
    )
    @DisplayName("должен удалить автора по id")
    void delete() {
        service.delete(AUTHORS_ID);

        verify(repository, times(1)).deleteById(AUTHORS_ID);
    }

    @Test
    @WithAnonymousUser
    @DisplayName("удаление не возможно для анонимного пользователя")
    void deleteFail() {
        assertThrows(AccessDeniedException.class, () -> service.delete(AUTHORS_ID));
    }

    @Test
    @DisplayName("должен найти автора по имени")
    void findByName() {
        when(repository.findByName(AUTHORS_NAME)).thenReturn(AUTHOR);
        when(mapper.toDto(AUTHOR))
                .thenReturn(AUTHORS_RESPONSE);

        var result = service.findByName(AUTHORS_NAME);

        assertEquals(AUTHORS_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти всех авторов")
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(AUTHOR));
        when(mapper.toDto(AUTHOR))
                .thenReturn(AUTHORS_RESPONSE);

        var result = service.findAll();

        result.forEach(item -> assertEquals(AUTHORS_RESPONSE, item));
    }
}