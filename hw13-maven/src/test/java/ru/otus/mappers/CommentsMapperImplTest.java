package ru.otus.mappers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.entities.Book;
import ru.otus.entities.Comment;
import ru.otus.repositories.BooksRepository;
import ru.otus.repositories.UsersRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@DisplayName("Mapper для работы с комментариями должен")
@SpringBootTest(classes = {
        CommentsMapperImpl.class,
        BookRequestMapper.class
})
class CommentsMapperImplTest {

    private final static Long TEST_COMMENT_ID = 1L;
    private final static Long TEST_BOOK_ID = 1L;
    private final static String TEST_COMMENT = "TEST_COMMENT";
    private final static String TEST_BOOK_NAME = "books_name";
    private final static CommentsRequest TEST_COMMENT_REQUEST = new CommentsRequest(TEST_COMMENT, TEST_BOOK_ID);
    private final static Book TEST_BOOK = new Book(
            TEST_BOOK_ID,
            TEST_BOOK_NAME,
            List.of(),
            List.of(),
            List.of());
    private final static Comment TEST_COMMENT_OBJECT = new Comment(TEST_COMMENT_ID, TEST_COMMENT, TEST_BOOK, null);

    @MockBean
    private BooksRepository booksRepository;
    @MockBean
    private UsersRepository usersRepository;

    @Autowired
    private CommentsMapper mapper;

    @Test
    @DisplayName("создавать Comment на основе CommentsRequest")
    void create() {
        when(booksRepository.findById(TEST_COMMENT_REQUEST.getBookId())).thenReturn(Optional.of(TEST_BOOK));

        var result = mapper.create(TEST_COMMENT_REQUEST, null);

        assertEquals(TEST_COMMENT, result.getComment());
        assertNotNull(result.getBook());
        assertEquals(TEST_BOOK_NAME, result.getBook().getName());
        assertEquals(TEST_BOOK_ID, result.getBook().getId());
    }

    @Test
    @DisplayName("обновлять Comment на основе CommentsRequest")
    void update() {
        when(booksRepository.findById(TEST_COMMENT_REQUEST.getBookId())).thenReturn(Optional.of(TEST_BOOK));

        var result = new Comment();

        mapper.update(result, TEST_COMMENT_REQUEST);

        assertEquals(TEST_COMMENT, result.getComment());
        assertNotNull(result.getBook());
        assertEquals(TEST_BOOK_NAME, result.getBook().getName());
        assertEquals(TEST_BOOK_ID, result.getBook().getId());
    }

    @Test
    @DisplayName("создавать CommentsResponse на основе Comment")
    void toDto() {
        var result = mapper.toDto(TEST_COMMENT_OBJECT);

        assertEquals(TEST_COMMENT_OBJECT.getId(), result.getId());
        assertEquals(TEST_COMMENT_OBJECT.getComment(), result.getComment());
    }
}