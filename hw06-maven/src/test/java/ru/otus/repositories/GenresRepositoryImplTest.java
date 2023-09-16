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
import ru.otus.entities.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Репозиторий на основе Jpa для работы с авторами ")
@ExtendWith(MockitoExtension.class)
class GenresRepositoryImplTest {

    private static final Long GENRE_ID = 1L;
    private static final String GENRE_NAME = "drama";
    private static final Genre TEST = new Genre(GENRE_ID, GENRE_NAME);
    private static final List<Genre> TEST_LIST = List.of(TEST);
    private static final Long TEST_INT = (long) TEST_LIST.size();

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Genre> query;
    @Mock
    private TypedQuery<Long> countQuery;

    private GenresRepository repository;

    @BeforeEach
    void setUp() {
        repository = new GenresRepositoryImpl(entityManager);
    }

    @Test
    @DisplayName("при поиске по имени должен выполнить sql запрос к БД и вернуть жанр по имени")
    void getByName() {
        when(
                entityManager.createQuery(
                        eq("SELECT g FROM Genre g WHERE g.name = :name"),
                        eq(Genre.class)))
                .thenReturn(query);
        when(query.setParameter("name", GENRE_NAME))
                .thenReturn(query);
        when(query.getSingleResult())
                .thenReturn(TEST);

        var result = repository.getByName(GENRE_NAME);

        assertEquals(TEST, result);
        verify(entityManager, times(1))
                .createQuery(
                        eq("SELECT g FROM Genre g WHERE g.name = :name"),
                        eq(Genre.class));
        verify(query, times(1)).setParameter(eq("name"), eq(GENRE_NAME));
        verify(query, times(1)).getSingleResult();
    }

    @Test
    @DisplayName("должен выполнить sql запрос к БД и вернуть жанр по id")
    void getById() {
        when(entityManager.find(eq(Genre.class), eq(GENRE_ID))).thenReturn(TEST);

        var result = repository.getById(GENRE_ID);

        assertEquals(TEST, result.orElse(null));
        verify(entityManager, times(1)).find(eq(Genre.class), eq(GENRE_ID));
    }

    @Test
    @DisplayName("должен выполнить sql запрос к БД и вернуть список жанров")
    void getAll() {
        when(
                entityManager.createQuery(
                        eq("SELECT g FROM Genre g"),
                        eq(Genre.class)))
                .thenReturn(query);
        when(query.getResultList())
                .thenReturn(TEST_LIST);

        var result = repository.getAll();

        assertEquals(TEST_LIST, result);
        verify(entityManager, times(1))
                .createQuery(
                        eq("SELECT g FROM Genre g"),
                        eq(Genre.class));
        verify(query, times(1)).getResultList();
    }

    @Test
    @DisplayName("при создании должен создать в БД и вернуть жанр")
    void create() {
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(entityManager)
                .persist(any());

        var result = repository.create(TEST);

        assertEquals(TEST, result);
        verify(entityManager, times(1)).persist(eq(TEST));
    }

    @Test
    @DisplayName("при обновлении должен сохранить в БД и вернуть жанр")
    void update() {
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(entityManager)
                .merge(any());

        var result = repository.update(TEST);

        assertEquals(TEST, result);
        verify(entityManager, times(1)).merge(eq(TEST));
    }

    @Test
    @DisplayName("удаляет")
    void delete() {
        when(
                entityManager.find(Genre.class, GENRE_ID))
                .thenReturn(TEST);

        repository.delete(GENRE_ID);

        verify(entityManager, times(1)).find(Genre.class, GENRE_ID);
        verify(entityManager, times(1)).remove(TEST);
    }

    @Test
    @DisplayName("при проверке на существование должен выполнить sql запрос к БД и вернуть логическое значение")
    void existTrue() {
        when(
                entityManager.createQuery(
                        eq("select count(g) from Genre g where g.id = :id"),
                        eq(Long.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter("id", GENRE_ID)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(TEST_INT);

        var result = repository.exist(GENRE_ID);

        assertTrue(result);
        verify(entityManager, times(1))
                .createQuery(
                        eq("select count(g) from Genre g where g.id = :id"),
                        eq(Long.class));
        verify(countQuery, times(1)).setParameter(eq("id"), eq(GENRE_ID));
        verify(countQuery, times(1)).getSingleResult();
    }

    @Test
    @DisplayName("при проверке на существование должен выполнить sql запрос к БД и вернуть логическое значение")
    void existFalse() {
        when(entityManager.createQuery(
                "select count(g) from Genre g where g.id = :id",
                Long.class))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("id"), eq(GENRE_ID))).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        var result = repository.exist(GENRE_ID);

        assertFalse(result);
        verify(entityManager, times(1))
                .createQuery(
                        eq("select count(g) from Genre g where g.id = :id"),
                        eq(Long.class));
        verify(countQuery, times(1)).setParameter(eq("id"), eq(GENRE_ID));
        verify(countQuery, times(1)).getSingleResult();
    }

    @Test
    @DisplayName("при получении количества выполнить sql запрос к БД и вернуть число")
    void count() {
        when(
                entityManager.createQuery(
                        eq("select count(g.id) from Genre g"),
                        eq(Long.class)))
                .thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(TEST_INT);

        var result = repository.count();

        assertEquals(TEST_INT.intValue(), result);
        verify(entityManager, times(1))
                .createQuery(
                        eq("select count(g.id) from Genre g"),
                        eq(Long.class));
        verify(countQuery, times(1)).getSingleResult();
    }

    @Test
    @DisplayName("при проверке на то что Автор уже существует должен выполнить sql запрос к БД и вернуть логическое значение")
    void existNameTrue() {
        when(
                entityManager.createQuery(
                        eq("select count(g) from Genre g where g.name = :name"),
                        eq(Long.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter("name", GENRE_NAME)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(TEST_INT);

        var result = repository.existName(GENRE_NAME);

        assertTrue(result);
        verify(entityManager, times(1))
                .createQuery(
                        eq("select count(g) from Genre g where g.name = :name"),
                        eq(Long.class));
        verify(countQuery, times(1)).setParameter(eq("name"), eq(GENRE_NAME));
        verify(countQuery, times(1)).getSingleResult();
    }

    @Test
    @DisplayName("при проверке на то что Автор уже существует должен выполнить sql запрос к БД и вернуть логическое значение")
    void existNameFalse() {
        when(entityManager.createQuery(
                "select count(g) from Genre g where g.name = :name",
                Long.class))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("name"), eq(GENRE_NAME))).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        var result = repository.existName(GENRE_NAME);

        assertFalse(result);
        verify(entityManager, times(1))
                .createQuery(
                        eq("select count(g) from Genre g where g.name = :name"),
                        eq(Long.class));
        verify(countQuery, times(1)).setParameter(eq("name"), eq(GENRE_NAME));
        verify(countQuery, times(1)).getSingleResult();
    }

    @Test
    @DisplayName("обрабатывает NoResultException")
    void existNameNoResult() {
        when(entityManager.createQuery(any(), any()))
                .thenThrow(new NoResultException());

        assertFalse(repository.existName(GENRE_NAME));
        assertFalse(repository.exist(GENRE_ID));
        assertEquals(0, repository.count());
    }
}