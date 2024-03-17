package ru.otus.mappers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.mongo.repositories.AuthorsMongoRepository;
import ru.otus.mongo.repositories.GenresMongoRepository;
import ru.otus.postgre.entities.Author;
import ru.otus.postgre.entities.Book;
import ru.otus.postgre.entities.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        AuthorsMongoRepository.class,
        GenresMongoRepository.class,
        BookRequestMapperImpl.class
})
class BookRequestMapperImplTest {

    private final static String TEST_BOOK_NAME = "name";
    private final static String TEST_AUTHORS_NAME = "Authors";
    private final static String TEST_GENRES_NAME = "Genres";
    private final static Author TEST_AUTHORS = new Author(null, TEST_AUTHORS_NAME);
    private final static Genre TEST_GENRES = new Genre(null, TEST_GENRES_NAME);
    private final static Book TEST_BOOK = new Book(
            null,
            TEST_BOOK_NAME,
            List.of(TEST_AUTHORS),
            List.of(TEST_GENRES),
            null);

    @MockBean
    private AuthorsMongoRepository authorsRepository;
    @MockBean
    private GenresMongoRepository genresRepository;

    @Autowired
    private BookRequestMapper mapper;

    @Test
    @DisplayName("Преобразует JPA Entity в Mongo документ")
    void create() {
        Mockito.when(authorsRepository.findByName(TEST_AUTHORS_NAME)).thenReturn(new ru.otus.mongo.entities.Author(null, TEST_AUTHORS_NAME));
        Mockito.when(genresRepository.findByName(TEST_GENRES_NAME)).thenReturn(new ru.otus.mongo.entities.Genre(null, TEST_GENRES_NAME));

        var result = mapper.create(TEST_BOOK);


        assertNotNull(result);
        assertEquals(TEST_BOOK_NAME, result.getName());
        Assertions.assertThat(result.getAuthors())
                .map(ru.otus.mongo.entities.Author::getName)
                .contains(TEST_AUTHORS_NAME);
        Assertions.assertThat(result.getGenres())
                .map(ru.otus.mongo.entities.Genre::getName)
                .contains(TEST_GENRES_NAME);
    }

    @Test
    @DisplayName("Если автор ещё не перенесены должен выдать ошибку")
    void creatAuthorFail() {
        Mockito.when(authorsRepository.findByName(TEST_AUTHORS_NAME)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> mapper.create(TEST_BOOK), "Неизвестный автор");
    }

    @Test
    @DisplayName("Если жанр ещё не перенесены должен выдать ошибку")
    void creatGenreFail() {
        Mockito.when(authorsRepository.findByName(TEST_AUTHORS_NAME)).thenReturn(new ru.otus.mongo.entities.Author(null, TEST_AUTHORS_NAME));
        Mockito.when(genresRepository.findByName(TEST_GENRES_NAME)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> mapper.create(TEST_BOOK), "Неизвестный жанр");
    }
}