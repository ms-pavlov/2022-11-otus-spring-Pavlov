package ru.otus.processors;

import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.mappers.BookRequestMapper;
import ru.otus.model.MapModel;
import ru.otus.postgre.entities.Book;

@Component("bookProcessor")
@AllArgsConstructor
public class BookProcessor implements ItemProcessor<Book, MapModel<ru.otus.mongo.entities.Book, Long>> {

    private final BookRequestMapper mapper;

    @Override
    public MapModel<ru.otus.mongo.entities.Book, Long> process(@NonNull Book item) {
        return MapModel.<ru.otus.mongo.entities.Book, Long>builder()
                .id(item.getId())
                .object(mapper.create(item))
                .build();
    }
}
