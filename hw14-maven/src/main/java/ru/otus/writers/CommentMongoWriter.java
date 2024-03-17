package ru.otus.writers;

import lombok.AllArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.mongo.entities.Comment;
import ru.otus.mongo.repositories.CommentsMongoRepository;

@Component
@AllArgsConstructor
public class CommentMongoWriter implements ItemWriter<Comment> {

    private final CommentsMongoRepository commentsMongoRepository;

    @Override
    public void write(@NonNull Chunk<? extends Comment> chunk) {
        commentsMongoRepository.saveAll(chunk);
    }
}
