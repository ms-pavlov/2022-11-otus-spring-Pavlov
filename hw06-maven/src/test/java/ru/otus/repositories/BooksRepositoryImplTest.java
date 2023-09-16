package ru.otus.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@ExtendWith(MockitoExtension.class)
class BooksRepositoryImplTest {

    private static final Long EXPECTED_BOOKS_COUNT = 1L;
    private static final Long EXISTING_BOOKS_ID = 1L;
    private static final Long OTHER_BOOKS_ID = EXISTING_BOOKS_ID + 2L;
    private static final String EXISTING_BOOKS_NAME = "books_name";
    private static final String OTHER_BOOKS_NAME = "books_name_alt";
    private static final String EXISTING_AUTHOR_NAME = "Ivan";
    private static final String OTHER_AUTHOR_NAME = "Petr";
    private static final String EXISTING_GENRES_NAME = "drama";
    private static final String OTHER_GENRES_NAME = "comedy";
    private static final Book TEST_BOOK = new Book(
            EXISTING_BOOKS_ID,
            EXISTING_BOOKS_NAME,
            List.of(new Author(null, EXISTING_AUTHOR_NAME)),
            List.of(new Genre(null, EXISTING_GENRES_NAME)));
    private static final Book OTHER_TEST_BOOK = new Book(
            OTHER_BOOKS_ID,
            OTHER_BOOKS_NAME,
            List.of(new Author(null, OTHER_AUTHOR_NAME)),
            List.of(new Genre(null, OTHER_GENRES_NAME)));
    private static final List<Book> TEST_BOOKS = List.of(TEST_BOOK, OTHER_TEST_BOOK);

    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<Book> query;
    @Mock
    private TypedQuery<Long> countQuery;
    private BooksRepository booksRepository;


    @BeforeEach
    void setUp() {
        booksRepository = new BooksRepositoryImpl(entityManager);
    }

    @Test
    @DisplayName("при поиске по имени должен выполнить sql запрос к БД и вернуть книгу по названию")
    void getByName() {
        when(
                entityManager.createQuery(
                        eq("SELECT b FROM Book b WHERE b.name = :name"),
                        eq(Book.class)))
                .thenReturn(query);
        when(query.setParameter("name", EXISTING_BOOKS_NAME))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(TEST_BOOKS);

        var result = booksRepository.getByName(EXISTING_BOOKS_NAME);

        assertTrue(TEST_BOOKS.containsAll(result));
        verify(entityManager, times(1))
                .createQuery(
                        eq("SELECT b FROM Book b WHERE b.name = :name"),
                        eq(Book.class));
        verify(query, times(1)).setParameter(eq("name"), eq(EXISTING_BOOKS_NAME));
        verify(query, times(1)).getResultList();
    }

    @Test
    @DisplayName("должен выполнить sql запрос к БД и вернуть книгу по id")
    void getById() {
        when(entityManager.find(eq(Book.class), eq(EXISTING_BOOKS_ID))).thenReturn(TEST_BOOK);

        var result = booksRepository.getById(EXISTING_BOOKS_ID);

        assertEquals(TEST_BOOK, result.orElse(null));
        verify(entityManager, times(1)).find(eq(Book.class), eq(EXISTING_BOOKS_ID));
    }

    @Test
    @DisplayName("должен выполнить sql запрос к БД и вернуть список книг")
    void getAll() {
        when(
                entityManager.createQuery(
                        eq("SELECT b FROM Book b"),
                        eq(Book.class)))
                .thenReturn(query);
        when(query.getResultList())
                .thenReturn(TEST_BOOKS);

        var result = booksRepository.getAll();

        assertEquals(TEST_BOOKS, result);
        verify(entityManager, times(1))
                .createQuery(
                        eq("SELECT b FROM Book b"),
                        eq(Book.class));
        verify(query, times(1)).getResultList();
    }

    @Test
    @DisplayName("при создании должен создать в БД и вернуть книгу")
    void create() {
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(entityManager)
                .persist(any());

        var result = booksRepository.create(TEST_BOOK);

        assertEquals(TEST_BOOK, result);
        verify(entityManager, times(1)).persist(eq(TEST_BOOK));
    }

    @Test
    @DisplayName("при обновлении должен сохранить в БД и вернуть книгу")
    void update() {
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(entityManager)
                .merge(any());

        var result = booksRepository.update(TEST_BOOK);

        assertEquals(TEST_BOOK, result);
        verify(entityManager, times(1)).merge(eq(TEST_BOOK));
    }

    @Test
    void delete() {
        when(
                entityManager.find(Book.class, EXISTING_BOOKS_ID))
                .thenReturn(TEST_BOOK);

        booksRepository.delete(EXISTING_BOOKS_ID);

        verify(entityManager, times(1)).find(Book.class, EXISTING_BOOKS_ID);
        verify(entityManager, times(1)).remove(TEST_BOOK);
    }

    @Test
    @DisplayName("при проверке на существование должен выполнить sql запрос к БД и вернуть логическое значение")
    void existTrue() {
        when(
                entityManager.createQuery(
                        eq("select count(b.id) from Book b where b.id = :id"),
                        eq(Long.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter("id", EXISTING_BOOKS_ID)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(EXPECTED_BOOKS_COUNT);

        var result = booksRepository.exist(EXISTING_BOOKS_ID);

        assertTrue(result);
        verify(entityManager, times(1))
                .createQuery(
                        eq("select count(b.id) from Book b where b.id = :id"),
                        eq(Long.class));
        verify(countQuery, times(1)).setParameter(eq("id"), eq(EXISTING_BOOKS_ID));
        verify(countQuery, times(1)).getSingleResult();
    }

    @Test
    @DisplayName("при проверке на существование должен выполнить sql запрос к БД и вернуть логическое значение")
    void existFalse() {
        when(
                entityManager.createQuery(
                        eq("select count(b.id) from Book b where b.id = :id"),
                        eq(Long.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter("id", EXISTING_BOOKS_ID)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        var result = booksRepository.exist(EXISTING_BOOKS_ID);

        assertFalse(result);
        verify(entityManager, times(1))
                .createQuery(
                        eq("select count(b.id) from Book b where b.id = :id"),
                        eq(Long.class));
        verify(countQuery, times(1)).setParameter(eq("id"), eq(EXISTING_BOOKS_ID));
        verify(countQuery, times(1)).getSingleResult();
    }

    @Test
    @DisplayName("при получении количества выполнить sql запрос к БД и вернуть число")
    void count() {
        when(
                entityManager.createQuery(
                        eq("select count(b.id) from Book b"),
                        eq(Long.class)))
                .thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(EXPECTED_BOOKS_COUNT);

        var result = booksRepository.count();

        assertEquals(EXPECTED_BOOKS_COUNT.intValue(), result);
        verify(entityManager, times(1))
                .createQuery(
                        eq("select count(b.id) from Book b"),
                        eq(Long.class));
        verify(countQuery, times(1)).getSingleResult();
    }

    @Test
    @DisplayName("обрабатывает NoResultException")
    void existNameNoResult() {
        when(entityManager.createQuery(any(), any()))
                .thenThrow(new NoResultException());

        assertFalse(booksRepository.exist(EXISTING_BOOKS_ID));
        assertEquals(0, booksRepository.count());
    }
}