package ru.otus.readers;

import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.otus.postgre.entities.Book;
import ru.otus.postgre.repositories.BooksRepository;

@Component
public class BookReader extends AbstractPagingItemReader<Book> {

    private final BooksRepository booksRepository;

    public BookReader(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    protected void doReadPage() {
        this.results = booksRepository.findAll(PageRequest.of(getPage(), getPageSize()))
                .getContent();
    }
}
