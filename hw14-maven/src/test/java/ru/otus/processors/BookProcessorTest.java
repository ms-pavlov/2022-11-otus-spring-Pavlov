package ru.otus.processors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.mappers.BookRequestMapper;
import ru.otus.postgre.entities.Book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {
        BookProcessor.class,
        BookRequestMapper.class
})
class BookProcessorTest {

    private final static Book BOOK = new Book(1L, "BOOK", null, null, null);
    private final static ru.otus.mongo.entities.Book MONGO_BOOK = new ru.otus.mongo.entities.Book();

    @MockBean
    private BookRequestMapper mapper;

    @Autowired
    private BookProcessor processor;

    @Test
    @DisplayName("Преобразует JPA Entity в Mongo документ")
    void process() {
        Mockito.when(mapper.create(BOOK)).thenReturn(MONGO_BOOK);


        var result = processor.process(BOOK);

        assertNotNull(result);
        assertEquals(MONGO_BOOK, result.object());
        assertEquals(BOOK.getId(), result.id());
    }
}