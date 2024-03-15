package ru.otus.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.postgre.entities.Genre;

@Component
public class GenreProcessor implements ItemProcessor<Genre, ru.otus.mongo.entities.Genre> {


    @Override
    public ru.otus.mongo.entities.Genre process(Genre item) {
        return new ru.otus.mongo.entities.Genre(null, item.getName());
    }
}
