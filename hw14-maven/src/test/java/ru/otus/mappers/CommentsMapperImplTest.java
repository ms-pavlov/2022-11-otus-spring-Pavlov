package ru.otus.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.mongo.entities.Book;
import ru.otus.postgre.entities.Comment;
import ru.otus.storages.BookCashStorage;

import java.util.List;

@SpringBootTest(classes = {
        BookCashStorage.class,
        CommentsMapperImpl.class
})
class CommentsMapperImplTest {

    private final static Long TEST_COMMENT_ID = 1L;
    private final static Long TEST_BOOK_ID = 1L;
    private final static String TEST_BOOK_STRING_ID = "1L";
    private final static String TEST_COMMENT = "TEST_COMMENT";
    private final static String TEST_BOOK_NAME = "books_name";
    private final static Book TEST_BOOK = new Book(
            TEST_BOOK_STRING_ID,
            TEST_BOOK_NAME,
            List.of(),
            List.of());
    private final static Comment TEST_COMMENT_OBJECT = new Comment(TEST_COMMENT_ID, TEST_COMMENT, new ru.otus.postgre.entities.Book(TEST_BOOK_ID, TEST_BOOK_NAME, null, null, null));

    @MockBean
    private BookCashStorage bookCashStorage;

    @Autowired
    private CommentsMapper mapper;


    @Test
    @DisplayName("Преобразует JPA Entity в Mongo документ")
    void create() {
        Mockito.when(bookCashStorage.get(TEST_BOOK_ID)).thenReturn(TEST_BOOK);

        var result = mapper.create(TEST_COMMENT_OBJECT);

        Assertions.assertEquals(TEST_COMMENT, result.getComment());
        Assertions.assertEquals(TEST_BOOK, result.getBook());

    }
}