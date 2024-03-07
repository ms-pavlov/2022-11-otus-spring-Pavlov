package ru.otus.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;

public interface BooksRepository extends ReactiveCrudRepository<Book, String> {

    Flux<Book> findByName(String name);

    Mono<Boolean> existsByAuthorsContains(Author author);

    Mono<Boolean> existsByGenresContains(Genre genre);

    Flux<Book> findAllBy(Pageable pageable);

}
