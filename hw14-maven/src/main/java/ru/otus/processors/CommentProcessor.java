package ru.otus.processors;

import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.mappers.CommentsMapper;
import ru.otus.postgre.entities.Comment;

@Component("commentProcessor")
@AllArgsConstructor
public class CommentProcessor implements ItemProcessor<Comment, ru.otus.mongo.entities.Comment> {

    private final CommentsMapper mapper;

    @Override
    public ru.otus.mongo.entities.Comment process(@NonNull Comment item) {
        return mapper.create(item);
    }
}
