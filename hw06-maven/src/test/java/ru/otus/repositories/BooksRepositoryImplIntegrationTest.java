package ru.otus.repositories;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import(BooksRepositoryImpl.class)
class BooksRepositoryImplIntegrationTest {

    private static final int EXPECTED_QUERIES_COUNT = 3;
    private static final Long EXPECTED_BOOKS_COUNT = 1L;
    private static final Long EXISTING_BOOKS_ID = 1L;
    private static final Long OTHER_BOOKS_ID = EXISTING_BOOKS_ID + 2L;
    private static final String EXISTING_BOOKS_NAME = "books_name";
    private static final String OTHER_BOOKS_NAME = "books_name_alt";
    private static final String EXISTING_AUTHOR_NAME = "Ivan";
    private static final String OTHER_AUTHOR_NAME = "Petr";
    private static final String EXISTING_GENRES_NAME = "drama";
    private static final String OTHER_GENRES_NAME = "comedy";

    @Autowired
    private BooksRepository repository;

    @Autowired
    private TestEntityManager em;

    private SessionFactory sessionFactory;

    @BeforeEach
    void setUp() {
        sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
    }

    @Test
    @DisplayName("возвращать список книг по названию")
    void getByName() {
        var result = repository.getByName(EXISTING_BOOKS_NAME);

        assertEquals(EXPECTED_BOOKS_COUNT, result.size());
        assertEquals(EXISTING_BOOKS_NAME, result.get(0).getName());
        assertEquals(EXISTING_AUTHOR_NAME, result.get(0).getAuthors().get(0).getName());
//        assertEquals(EXISTING_GENRES_NAME, result.get(0).getGenres().get(0).getName());
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @Test
    @DisplayName("возвращать книгу по её id")
    void getById() {
        var result = repository.getById(EXISTING_BOOKS_ID);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(EXISTING_BOOKS_ID, result.map(Book::getId).orElse(null));
        assertEquals(EXISTING_BOOKS_NAME, result.map(Book::getName).orElse(null));
        assertEquals(
                EXISTING_AUTHOR_NAME,
                result
                        .map(Book::getAuthors)
                        .map(authors -> authors.get(0))
                        .map(Author::getName)
                        .orElse(null));
//        assertEquals(
//                EXISTING_GENRES_NAME,
//                result
//                        .map(Book::getGenres)
//                        .map(genres -> genres.get(0))
//                        .map(Genre::getName)
//                        .orElse(null));
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("возвращать ожидаемый список книг")
    void getAll() {
        var result = repository.getAll();

        assertEquals(EXPECTED_BOOKS_COUNT, result.size());
        assertEquals(EXISTING_AUTHOR_NAME, result.get(0).getAuthors().get(0).getName());
//        assertEquals(EXISTING_GENRES_NAME, result.get(0).getGenres().get(0).getName());
    }

    @Test
    @DisplayName("добавлять книги в БД")
    void create() {
        var book = new Book(
                null,
                OTHER_BOOKS_NAME,
                null,
                List.of(new Genre(null, OTHER_GENRES_NAME)));
        book.setAuthors(List.of(new Author(null, OTHER_AUTHOR_NAME)));

        var result = repository.create(book);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(OTHER_BOOKS_NAME, result.getName());
        assertFalse(result.getAuthors().isEmpty());
        assertNotNull(result.getAuthors().get(0).getId());
        assertEquals(OTHER_AUTHOR_NAME, result.getAuthors().get(0).getName());
        assertNotNull(result.getGenres().get(0).getId());
        assertEquals(OTHER_GENRES_NAME, result.getGenres().get(0).getName());
    }

    @Test
    void update() {
        var book = repository.getById(EXISTING_BOOKS_ID)
                .orElse(null);
        var author = new Author(null, OTHER_AUTHOR_NAME);
        var genre = new Genre(null, OTHER_GENRES_NAME);
        book.setName(OTHER_BOOKS_NAME);
        book.getAuthors().clear();
        book.getAuthors().add(author);
        book.getGenres().clear();
        book.getGenres().add(genre);

        book = repository.update(book);
        var result = repository.getById(book.getId()).orElse(null);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(OTHER_BOOKS_NAME, result.getName());
        assertFalse(result.getAuthors().isEmpty());
        assertEquals(1, result.getAuthors().size());
        assertEquals(OTHER_AUTHOR_NAME, result.getAuthors().get(0).getName());
        assertFalse(result.getGenres().isEmpty());
        assertEquals(1, result.getGenres().size());
        assertEquals(OTHER_GENRES_NAME, result.getGenres().get(0).getName());
    }


    @Test
    @DisplayName("удалять книгу по её id")
    void delete() {
        var book = repository.create(new Book(null, OTHER_BOOKS_NAME, List.of(), List.of()));

        assertTrue(repository.exist(book.getId()));

        repository.delete(book.getId());

        assertFalse(repository.exist(book.getId()));
        assertFalse(repository.getById(book.getId()).isPresent());
    }

    @Test
    @DisplayName("определять что книга с заданым id существует в БД")
    void exist() {
        assertTrue(repository.exist(EXISTING_BOOKS_ID));
        assertFalse(repository.exist(EXISTING_BOOKS_ID + OTHER_BOOKS_ID));
    }

    @Test
    @DisplayName("возвращать ожидаемое количество книг в БД")
    void count() {
        assertEquals(EXPECTED_BOOKS_COUNT, repository.count());
    }
}