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
import ru.otus.repositories.BooksRepository;

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

    private static final String AUTHORS_ID = "1L";
    private static final String AUTHORS_NAME = "name";
    private final static Author AUTHOR = new Author(
            AUTHORS_ID,
            AUTHORS_NAME);

    private final static Author OTHERT_AUTHOR = new Author(
            100L + AUTHORS_ID,
            "other" + AUTHORS_NAME);
    private final static AuthorsResponse AUTHORS_RESPONSE = new AuthorsResponse(
            AUTHORS_ID,
            AUTHORS_NAME);
    private final static AuthorsRequest AUTHOR_REQUEST = new AuthorsRequest(AUTHOR.getName());


    @MockBean
    private AuthorsRepository repository;
    @MockBean
    private BooksRepository booksRepository;
    @MockBean
    private AuthorsMapper mapper;
    @Autowired
    private AuthorsService service;

    @Test
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
    @DisplayName("должен проверить, что такой автор создан, и вернуть исключение")
    void createExistAuthor() {
        when(repository.findByName(AUTHORS_NAME)).thenReturn(AUTHOR);

        assertThrows(AuthorExistException.class, () -> service.create(AUTHOR_REQUEST));
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
    @DisplayName("должен проверить, что такой автор создан, и вернуть исключение")
    void updateExistAuthor() {
        when(repository.findByName(AUTHORS_NAME)).thenReturn(OTHERT_AUTHOR);

        assertThrows(AuthorExistException.class, () -> service.update(AUTHORS_ID, AUTHOR_REQUEST));
    }

    @Test
    @DisplayName("должен удалить автора по id, если у автора нет книг")
    void deleteNoBook() {
        when(repository.findById(AUTHORS_ID)).thenReturn(Optional.of(AUTHOR));
        when(booksRepository.existsByAuthorsContains(AUTHOR)).thenReturn(false);

        service.delete(AUTHORS_ID);

        verify(repository, times(1)).findById(AUTHORS_ID);
        verify(booksRepository, times(1)).existsByAuthorsContains(AUTHOR);
        verify(repository, times(1)).deleteById(AUTHORS_ID);
    }

    @Test
    @DisplayName("должен вернуть исключение, если при удалении у автора есть книги")
    void deleteHasBook() {
        when(repository.findById(AUTHORS_ID)).thenReturn(Optional.of(AUTHOR));
        when(booksRepository.existsByAuthorsContains(AUTHOR)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> service.delete(AUTHORS_ID));

        verify(repository, times(1)).findById(AUTHORS_ID);
        verify(booksRepository, times(1)).existsByAuthorsContains(AUTHOR);
        verify(repository, times(0)).deleteById(AUTHORS_ID);
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