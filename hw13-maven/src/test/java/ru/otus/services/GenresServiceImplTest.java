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
import ru.otus.dto.requests.GenresRequest;
import ru.otus.dto.responses.GenresResponse;
import ru.otus.entities.Genre;
import ru.otus.mappers.GenresMapper;
import ru.otus.repositories.GenresRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Service для работы с жанрами должен")
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
class GenresServiceImplTest {

    private static final Long GENRES_ID = 1L;
    private static final Long OTHER_GENRES_ID = 100L + GENRES_ID;
    private static final String GENRES_NAME = "name";
    private static final String OTHER_GENRES_NAME = "other_" + GENRES_NAME;
    private final static Genre GENRE = new Genre(
            GENRES_ID,
            GENRES_NAME);
    private final static Genre OTHER_GENRE = new Genre(
            OTHER_GENRES_ID,
            OTHER_GENRES_NAME);
    private final static GenresResponse GENRES_RESPONSE = new GenresResponse(
            GENRES_ID,
            GENRES_NAME,
            List.of());
    private final static GenresRequest GENRE_REQUEST = new GenresRequest(GENRE.getName());


    @MockBean
    private GenresRepository repository;
    @MockBean
    private GenresMapper mapper;
    @MockBean
    private UsersService usersService;

    @Autowired
    private GenresService service;

    @Test
    @WithMockUser(
            roles = "ADMIN"
    )
    @DisplayName("должен проверить что такой жанр ещё не создан, сохранить и вернуть результат")
    void createByAdmin() {
        when(repository.findByName(GENRES_NAME)).thenReturn(null);
        when(mapper.create(GENRE_REQUEST)).thenReturn(GENRE);
        when(repository.save(GENRE))
                .thenReturn(GENRE);
        when(mapper.toDto(GENRE))
                .thenReturn(GENRES_RESPONSE);

        var result = service.create(GENRE_REQUEST);

        assertEquals(GENRES_RESPONSE, result);
        verify(repository, times(1)).findByName(GENRES_NAME);
        verify(repository, times(1)).save(GENRE);
        verify(mapper, times(1)).create(GENRE_REQUEST);
        verify(mapper, times(1)).toDto(GENRE);
    }

    @Test
    @WithAnonymousUser
    @DisplayName("создание не возможно для анонимного пользователя")
    void createFail() {
        assertThrows(AccessDeniedException.class, () -> service.create(GENRE_REQUEST));
    }

    @Test
    @WithMockUser(
            roles = "ADMIN"
    )
    @DisplayName("должен проверить, что такой жанр создан, и вернуть исключение")
    void createExistGenre() {
        when(repository.findByName(GENRES_NAME)).thenReturn(GENRE);

        assertThrows(GenreExistException.class, () -> service.create(GENRE_REQUEST));
    }

    @Test
    @DisplayName("должен найти жанр по id и вернуть результат")
    void findById() {
        when(repository.findById(GENRES_ID)).thenReturn(Optional.of(GENRE));
        when(mapper.toDto(GENRE))
                .thenReturn(GENRES_RESPONSE);

        var result = service.findById(GENRES_ID);

        assertEquals(GENRES_RESPONSE, result);
    }

    @Test
    @WithMockUser(
            roles = "ADMIN"
    )
    @DisplayName("должен найти жанр по id, установить новые значения из запроса, обновить и вернуть результат")
    void update() {
        when(repository.findById(GENRES_ID))
                .thenReturn(Optional.of(GENRE));
        doNothing().when(mapper).update(GENRE, GENRE_REQUEST);
        when(repository.save(GENRE))
                .thenReturn(GENRE);
        when(mapper.toDto(GENRE))
                .thenReturn(GENRES_RESPONSE);

        var result = service.update(GENRES_ID, GENRE_REQUEST);

        assertEquals(GENRES_RESPONSE, result);
        verify(mapper, times(1)).update(GENRE, GENRE_REQUEST);
        verify(repository, times(1)).save(GENRE);
    }

    @Test
    @WithAnonymousUser
    @DisplayName("изменение не возможно для анонимного пользователя")
    void updateFail() {
        assertThrows(AccessDeniedException.class, () -> service.update(GENRES_ID, GENRE_REQUEST));
    }

    @Test
    @WithMockUser(
            roles = "ADMIN"
    )
    @DisplayName("должен проверить, что такой жанр создан, и вернуть исключение")
    void updateExistGenre() {
        when(repository.findByName(GENRES_NAME)).thenReturn(OTHER_GENRE);

        assertThrows(GenreExistException.class, () -> service.update(GENRES_ID, GENRE_REQUEST));
    }

    @Test
    @WithMockUser(
            roles = "ADMIN"
    )
    @DisplayName("должен удалить жанр по id")
    void delete() {
        service.delete(GENRES_ID);

        verify(repository, times(1)).deleteById(GENRES_ID);
    }

    @Test
    @WithAnonymousUser
    @DisplayName("удаление не возможно для анонимного пользователя")
    void deleteFail() {
        assertThrows(AccessDeniedException.class, () -> service.delete(GENRES_ID));
    }


    @Test
    @DisplayName("должен найти жанр по названию")
    void findByName() {
        when(repository.findByName(GENRES_NAME)).thenReturn(GENRE);
        when(mapper.toDto(GENRE))
                .thenReturn(GENRES_RESPONSE);

        var result = service.findByName(GENRES_NAME);

        assertEquals(GENRES_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти все жанры")
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(GENRE));
        when(mapper.toDto(GENRE))
                .thenReturn(GENRES_RESPONSE);

        var result = service.findAll();

        result.forEach(item -> assertEquals(GENRES_RESPONSE, item));
    }
}