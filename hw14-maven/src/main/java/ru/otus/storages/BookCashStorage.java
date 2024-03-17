package ru.otus.storages;

import org.springframework.stereotype.Component;
import ru.otus.mongo.entities.Book;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BookCashStorage implements CashStorage<Book, Long> {

    private final Map<Long, Book> books;

    public BookCashStorage() {
        this.books = new ConcurrentHashMap<>();
    }


    @Override
    public synchronized void put(Long id, Book book) {
        books.put(id, book);
    }

    @Override
    public Book get(Long id) {
        return books.get(id);
    }
}
