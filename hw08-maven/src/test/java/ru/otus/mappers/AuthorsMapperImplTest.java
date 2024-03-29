package ru.otus.mappers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dto.requests.AuthorsRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.entities.Author;
import ru.otus.entities.Book;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;

@DisplayName("Mapper для работы с авторами должен")
@SpringBootTest(classes = {
        AuthorsMapperImpl.class
})
class AuthorsMapperImplTest {

    private final static String TEST_AUTHORS_ID = "1L";
    private final static String TEST_AUTHORS_NAME = "Authors";
    private final static String TEST_BOOK_NAME = "name";
    private final static Book TEST_BOOK = new Book(
            null,
            TEST_BOOK_NAME,
            List.of(),
            List.of());
    private final static Author TEST_AUTHORS = new Author(TEST_AUTHORS_ID, TEST_AUTHORS_NAME);
    private final static AuthorsRequest TEST_AUTHORS_REQUEST = new AuthorsRequest(TEST_AUTHORS_NAME);

    @MockBean
    private BookRequestMapper bookMapper;

    @Autowired
    private AuthorsMapper mapper;

    @Test
    @DisplayName("создавать Author на основе AuthorsRequest")
    void create() {

        var result = mapper.create(TEST_AUTHORS_REQUEST);

        assertEquals(TEST_AUTHORS_NAME, result.getName());
    }

    @Test
    @DisplayName("обновлять Author на основе AuthorsRequest")
    void update() {
        var result = new Author();

        mapper.update(result, TEST_AUTHORS_REQUEST);

        assertEquals(TEST_AUTHORS_NAME, result.getName());
    }

    @Test
    @DisplayName("создавать AuthorsResponse на основе Author")
    void toDto() {
        doAnswer(invocationOnMock -> {
            Book book = invocationOnMock.getArgument(0);
            return new BooksResponse(null, book.getName(), null, null);
        }).when(bookMapper).toDto(eq(TEST_BOOK));

        var result = mapper.toDto(TEST_AUTHORS);

        assertEquals(TEST_AUTHORS.getId(), result.getId());
        assertEquals(TEST_AUTHORS.getName(), result.getName());
    }
}