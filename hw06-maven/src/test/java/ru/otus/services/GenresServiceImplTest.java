package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@SpringBootTest(classes = {
        GenresServiceImpl.class
})
class GenresServiceImplTest {

    private static final Long GENRES_ID = 1L;
    private static final String GENRES_NAME = "name";
    private final static Genre GENRE = new Genre(
            GENRES_ID,
            GENRES_NAME);
    private final static GenresResponse GENRES_RESPONSE = new GenresResponse(
            GENRES_ID,
            GENRES_NAME,
            List.of());
    private final static GenresRequest GENRE_REQUEST = new GenresRequest(GENRE.getName());


    @MockBean
    private GenresRepository repository;
    @MockBean
    private GenresMapper mapper;
    @Autowired
    private GenresService service;

    @Test
    @DisplayName("должен проверить что такой жанр ещё не создан, сохранить и вернуть результат")
    void create() {
        when(repository.existName(GENRES_NAME)).thenReturn(false);
        when(mapper.create(GENRE_REQUEST)).thenReturn(GENRE);
        when(repository.create(GENRE))
                .thenReturn(GENRE);
        when(mapper.toDto(GENRE))
                .thenReturn(GENRES_RESPONSE);

        var result = service.create(GENRE_REQUEST);

        assertEquals(GENRES_RESPONSE, result);
        verify(repository, times(1)).existName(GENRES_NAME);
        verify(repository, times(1)).create(GENRE);
        verify(mapper, times(1)).create(GENRE_REQUEST);
        verify(mapper, times(1)).toDto(GENRE);
    }

    @Test
    @DisplayName("должен проверить, что такой жанр создан, и вернуть исключение")
    void createExistGenre() {
        when(repository.existName(GENRES_NAME)).thenReturn(true);

        assertThrows(GenreExistException.class, () -> service.create(GENRE_REQUEST));
    }

    @Test
    @DisplayName("должен найти жанр по id и вернуть результат")
    void findById() {
        when(repository.getById(GENRES_ID)).thenReturn(Optional.of(GENRE));
        when(mapper.toDto(GENRE))
                .thenReturn(GENRES_RESPONSE);

        var result = service.findById(GENRES_ID);

        assertEquals(GENRES_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти жанр по id, установить новые значения из запроса, обновить и вернуть результат")
    void update() {
        when(repository.getById(GENRES_ID))
                .thenReturn(Optional.of(GENRE));
        doNothing().when(mapper).update(GENRE, GENRE_REQUEST);
        when(repository.update(GENRE))
                .thenReturn(GENRE);
        when(mapper.toDto(GENRE))
                .thenReturn(GENRES_RESPONSE);

        var result = service.update(GENRES_ID, GENRE_REQUEST);

        assertEquals(GENRES_RESPONSE, result);
        verify(mapper, times(1)).update(GENRE, GENRE_REQUEST);
        verify(repository, times(1)).update(GENRE);
    }

    @Test
    @DisplayName("должен проверить, что такой жанр создан, и вернуть исключение")
    void updateExistGenre() {
        when(repository.existName(GENRES_NAME)).thenReturn(true);

        assertThrows(GenreExistException.class, () -> service.update(GENRES_ID, GENRE_REQUEST));
    }

    @Test
    @DisplayName("должен удалить жанр по id")
    void delete() {
        service.delete(GENRES_ID);

        verify(repository, times(1)).delete(GENRES_ID);
    }

    @Test
    @DisplayName("должен найти жанр по названию")
    void findByName() {
        when(repository.getByName(GENRES_NAME)).thenReturn(GENRE);
        when(mapper.toDto(GENRE))
                .thenReturn(GENRES_RESPONSE);

        var result = service.findByName(GENRES_NAME);

        assertEquals(GENRES_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти все жанры")
    void findAll() {
        when(repository.getAll()).thenReturn(List.of(GENRE));
        when(mapper.toDto(GENRE))
                .thenReturn(GENRES_RESPONSE);

        var result = service.findAll();

        result.forEach(item -> assertEquals(GENRES_RESPONSE, item));
    }
}