package ru.otus.repositories;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.entities.Book;
import ru.otus.entities.Comment;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями ")
@DataJpaTest
@Import(CommentsRepositoryImpl.class)
class CommentsRepositoryImplIntegrationTest {

    private static final int EXPECTED_QUERIES_COUNT = 2;
    private static final Integer EXPECTED_COMMENTS_COUNT = 4;
    private static final Long EXISTING_BOOKS_ID = 1L;
    private static final Long EXISTING_COMMENT_ID = 1L;
    private static final String EXISTING_COMMENT = "comment 1-1";
    private static final String OTHER_COMMENT = EXISTING_COMMENT + "000";
    private static final String BOOK_NAME = "test";
    private static final String OTHER_BOOK_NAME = BOOK_NAME + "test";


    @Autowired
    private CommentsRepository repository;

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
    void getCommentsByBookId() {
    }

    @Test
    void getById() {
        var result = repository.getById(EXISTING_COMMENT_ID).orElse(null);

        assertNotNull(result);
        assertEquals(EXISTING_COMMENT_ID, result.getId());
        assertEquals(EXISTING_BOOKS_ID,  result.getBook().getId());
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @Test
    @DisplayName("возвращать ожидаемый список комментариев")
    void getAll() {
        var result = repository.getAll();

        assertEquals(EXPECTED_COMMENTS_COUNT, result.size());
        assertTrue( result.stream().anyMatch(comment ->  Objects.equals(comment.getComment(), EXISTING_COMMENT)));
        assertTrue( result.stream()
                .anyMatch(
                        comment -> Objects.equals(comment.getBook().getId(), EXISTING_BOOKS_ID)));
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("добавлять комментарии в БД")
    void create() {
        var comment = new Comment(null, OTHER_COMMENT);
        comment.setBook(new Book(null, BOOK_NAME, null, null, List.of(comment)));

        var result = repository.create(comment);

        assertEquals(OTHER_COMMENT, result.getComment());
        assertNotNull(result.getId());
        assertEquals(BOOK_NAME, result.getBook().getName());
        assertNull(result.getBook().getId());

        repository.delete(result.getId());
    }

    @Test
    @DisplayName("Обновляет комментарии в БД")
    void update() {
        var genre = repository.getById(EXISTING_COMMENT_ID)
                .map(value -> {
                    value.setComment(OTHER_COMMENT);
                    value.setBook(new Book(null, OTHER_BOOK_NAME, List.of(), List.of(), List.of(value)));
                    return value;
                })
                .orElse(null);

        var result = repository.update(genre);

        assertEquals(OTHER_COMMENT, result.getComment());
        assertNotNull(result.getId());
        assertEquals(OTHER_BOOK_NAME, result.getBook().getName());
        assertNull(result.getBook().getId());
    }

    @Test
    @DisplayName("возвращать ожидаемое количество комментариев в БД")
    void count() {
        assertEquals(EXPECTED_COMMENTS_COUNT, repository.count());
    }
}