package ru.otus.writers;

import lombok.AllArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.model.MapModel;
import ru.otus.mongo.entities.Book;
import ru.otus.mongo.repositories.BooksMongoRepository;
import ru.otus.storages.CashStorage;

@Component
@AllArgsConstructor
public class BookMongoWriter implements ItemWriter<MapModel<Book, Long>> {

    private final BooksMongoRepository repository;
    private final CashStorage<Book, Long> cashStorage;

    @Override
    public void write(@NonNull Chunk<? extends MapModel<Book, Long>> chunk) {
        chunk.forEach(
                item -> {
                    Book result = repository.save(item.object());
                    cashStorage.put(item.id(), result);
                });
    }
}
