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
import ru.otus.entities.Comment;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями ")
@ExtendWith(MockitoExtension.class)
class CommentsRepositoryImplTest {

    private static final Long EXISTING_BOOKS_ID = 1L;
    private static final Long EXISTING_COMMENT_ID = 1L;
    private static final String EXISTING_COMMENT = "comment 1-1";
    private static final Comment TEST = new Comment(EXISTING_COMMENT_ID, EXISTING_COMMENT);
    private static final List<Comment> TEST_LIST = List.of(TEST);
    private static final Long TEST_INT = (long) TEST_LIST.size();

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Comment> query;
    @Mock
    private TypedQuery<Long> countQuery;

    private CommentsRepository repository;

    @BeforeEach
    void setUp() {
        repository = new CommentsRepositoryImpl(entityManager);
    }
    @Test
    @DisplayName("при получении комментариев для книги должен выполнить sql запрос к БД и вернуть список")
    void getCommentsByBookId() {
        when(
                entityManager.createQuery(
                        eq("SELECT c FROM Comment c JOIN FETCH c.book WHERE c.book.id = :bookId"),
                        eq(Comment.class)))
                .thenReturn(query);
        when(query.setParameter("bookId", EXISTING_BOOKS_ID))
                .thenReturn(query);
        when(query.getResultList())
                .thenReturn(TEST_LIST);

        var result = repository.getCommentsByBookId(EXISTING_BOOKS_ID);

        assertEquals(TEST_LIST, result);
        verify(entityManager, times(1))
                .createQuery(
                        eq("SELECT c FROM Comment c JOIN FETCH c.book WHERE c.book.id = :bookId"),
                        eq(Comment.class));
        verify(query, times(1)).setParameter(eq("bookId"), eq(EXISTING_BOOKS_ID));
        verify(query, times(1)).getResultList();
    }

    @Test
    @DisplayName("должен выполнить sql запрос к БД и вернуть комментарий по id")
    void getById() {
        when(entityManager.find(eq(Comment.class), eq(EXISTING_COMMENT_ID))).thenReturn(TEST);

        var result = repository.getById(EXISTING_COMMENT_ID);

        assertEquals(TEST, result.orElse(null));
        verify(entityManager, times(1)).find(eq(Comment.class), eq(EXISTING_COMMENT_ID));
    }

    @Test
    @DisplayName("должен выполнить sql запрос к БД и вернуть список комментариев")
    void getAll() {
        when(
                entityManager.createQuery(
                        eq("SELECT c FROM Comment c JOIN FETCH c.book"),
                        eq(Comment.class)))
                .thenReturn(query);
        when(query.getResultList())
                .thenReturn(TEST_LIST);

        var result = repository.getAll();

        assertEquals(TEST_LIST, result);
        verify(entityManager, times(1))
                .createQuery(
                        eq("SELECT c FROM Comment c JOIN FETCH c.book"),
                        eq(Comment.class));
        verify(query, times(1)).getResultList();
    }

    @Test
    @DisplayName("при создании должен создать в БД и вернуть комментарий")
    void create() {
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(entityManager)
                .persist(any());

        var result = repository.create(TEST);

        assertEquals(TEST, result);
        verify(entityManager, times(1)).persist(eq(TEST));
    }

    @Test
    @DisplayName("при обновлении должен сохранить в БД и вернуть комментарий")
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
                entityManager.find(Comment.class, EXISTING_COMMENT_ID))
                .thenReturn(TEST);

        repository.delete(EXISTING_COMMENT_ID);

        verify(entityManager, times(1)).find(Comment.class, EXISTING_COMMENT_ID);
        verify(entityManager, times(1)).remove(TEST);
    }

    @Test
    @DisplayName("при получении количества выполнить sql запрос к БД и вернуть число")
    void count() {
        when(
                entityManager.createQuery(
                        eq("select count(c.id) from Comment c"),
                        eq(Long.class)))
                .thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(TEST_INT);

        var result = repository.count();

        assertEquals(TEST_INT.intValue(), result);
        verify(entityManager, times(1))
                .createQuery(
                        eq("select count(c.id) from Comment c"),
                        eq(Long.class));
        verify(countQuery, times(1)).getSingleResult();
    }

    @Test
    @DisplayName("обрабатывает NoResultException")
    void existNameNoResult() {
        when(entityManager.createQuery(any(), any()))
                .thenThrow(new NoResultException());

        assertEquals(0, repository.count());
    }
}