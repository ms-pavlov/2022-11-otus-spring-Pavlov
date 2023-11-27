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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Репозиторий на основе Jpa для работы с авторами ")
@ExtendWith(MockitoExtension.class)
class AuthorsRepositoryImplTest {

    private static final Long AUTHOR_ID = 1L;
    private static final String AUTHOR_NAME = "Ivan";
    private static final Author TEST = new Author(AUTHOR_ID, AUTHOR_NAME);
    private static final List<Author> TEST_LIST = List.of(TEST);
    private static final Long TEST_INT = (long) TEST_LIST.size();

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Author> query;
    @Mock
    private TypedQuery<Long> countQuery;

    private AuthorsRepository authorsRepository;

    @BeforeEach
    void setUp() {
        authorsRepository = new AuthorsRepositoryImpl(entityManager);
    }

    @Test
    @DisplayName("при поиске по имени должен выполнить sql запрос к БД и вернуть автора по имени")
    void findByName() {
        when(
                entityManager.createQuery(
                        eq("SELECT a FROM Author a WHERE a.name = :name"),
                        eq(Author.class)))
                .thenReturn(query);
        when(query.setParameter("name", AUTHOR_NAME))
                .thenReturn(query);
        when(query.getSingleResult())
                .thenReturn(TEST);

        var result = authorsRepository.getByName(AUTHOR_NAME);

        assertEquals(TEST, result);
        verify(entityManager, times(1))
                .createQuery(
                        eq("SELECT a FROM Author a WHERE a.name = :name"),
                        eq(Author.class));
        verify(query, times(1)).setParameter(eq("name"), eq(AUTHOR_NAME));
        verify(query, times(1)).getSingleResult();
    }

    @Test
    @DisplayName("должен выполнить sql запрос к БД и вернуть автора по id")
    void getById() {
        when(entityManager.find(eq(Author.class), eq(AUTHOR_ID))).thenReturn(TEST);

        var result = authorsRepository.getById(AUTHOR_ID);

        assertEquals(TEST, result.orElse(null));
        verify(entityManager, times(1)).find(eq(Author.class), eq(AUTHOR_ID));
    }

    @Test
    @DisplayName("должен выполнить sql запрос к БД и вернуть список авторов")
    void getAll() {
        when(
                entityManager.createQuery(
                        eq("SELECT a FROM Author a"),
                        eq(Author.class)))
                .thenReturn(query);
        when(query.getResultList())
                .thenReturn(TEST_LIST);

        var result = authorsRepository.getAll();

        assertEquals(TEST_LIST, result);
        verify(entityManager, times(1))
                .createQuery(
                        eq("SELECT a FROM Author a"),
                        eq(Author.class));
        verify(query, times(1)).getResultList();
    }

    @Test
    @DisplayName("при создании должен создать в БД и вернуть автора")
    void create() {
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(entityManager)
                .persist(any());

        var result = authorsRepository.create(TEST);

        assertEquals(TEST, result);
        verify(entityManager, times(1)).persist(eq(TEST));
    }

    @Test
    @DisplayName("при обновлении должен сохранить в БД и вернуть автора")
    void update() {
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(entityManager)
                .merge(any());

        var result = authorsRepository.update(TEST);

        assertEquals(TEST, result);
        verify(entityManager, times(1)).merge(eq(TEST));
    }

    @Test
    @DisplayName("удаляет")
    void delete() {
        when(
                entityManager.find(Author.class, AUTHOR_ID))
                .thenReturn(TEST);

        authorsRepository.delete(AUTHOR_ID);

        verify(entityManager, times(1)).find(Author.class, AUTHOR_ID);
        verify(entityManager, times(1)).remove(TEST);
    }

    @Test
    @DisplayName("при получении количества выполнить sql запрос к БД и вернуть число")
    void count() {
        when(
                entityManager.createQuery(
                        eq("select count(a.id) from Author a"),
                        eq(Long.class)))
                .thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(TEST_INT);

        var result = authorsRepository.count();

        assertEquals(TEST_INT.intValue(), result);
        verify(entityManager, times(1))
                .createQuery(
                        eq("select count(a.id) from Author a"),
                        eq(Long.class));
        verify(countQuery, times(1)).getSingleResult();
    }

    @Test
    @DisplayName("обрабатывает NoResultException")
    void existNameNoResult() {
        when(entityManager.createQuery(any(), any()))
                .thenThrow(new NoResultException());

        assertEquals(0, authorsRepository.count());
    }
}