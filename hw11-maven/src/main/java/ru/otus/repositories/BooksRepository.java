package ru.otus.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.otus.entities.Book;

public interface BooksRepository extends ReactiveCrudRepository<Book, String> {

    Flux<Book> findAllBy(Pageable pageable);

    Flux<Book> findAllByAuthorsContains(String author);

    Flux<Book> findAllByGenresContains(String genre);

}
