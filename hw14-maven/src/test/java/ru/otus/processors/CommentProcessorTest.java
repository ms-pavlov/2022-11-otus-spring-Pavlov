package ru.otus.processors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.mappers.CommentsMapper;
import ru.otus.postgre.entities.Comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {
        CommentProcessor.class,
        CommentsMapper.class
})
class CommentProcessorTest {


    private final static Comment COMMENT = new Comment(null, "COMMENT", null);
    private final static ru.otus.mongo.entities.Comment MONGO_COMMENT = new ru.otus.mongo.entities.Comment();

    @MockBean
    private CommentsMapper mapper;

    @Autowired
    private CommentProcessor processor;

    @Test
    @DisplayName("Преобразует JPA Entity в Mongo документ")
    void process() {
        Mockito.when(mapper.create(COMMENT)).thenReturn(MONGO_COMMENT);


        var result = processor.process(COMMENT);

        assertNotNull(result);
        assertEquals(MONGO_COMMENT, result);

    }
}