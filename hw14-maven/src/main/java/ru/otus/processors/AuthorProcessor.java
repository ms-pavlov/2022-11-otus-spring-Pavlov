package ru.otus.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.postgre.entities.Author;

@Component
public class AuthorProcessor implements ItemProcessor<Author, ru.otus.mongo.entities.Author> {
    @Override
    public ru.otus.mongo.entities.Author process(@NonNull Author item) {
        return new ru.otus.mongo.entities.Author(null, item.getName());
    }
}
