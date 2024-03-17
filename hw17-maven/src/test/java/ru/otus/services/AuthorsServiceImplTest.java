package ru.otus.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.entities.Book;
import ru.otus.mappers.BookRequestMapper;
import ru.otus.repositories.BooksRepository;

import java.util.List;

import static org.mockito.Mockito.when;

@DisplayName("Service для работы с авторами должен")
@SpringBootTest(classes = {
        AuthorsServiceImpl.class
})
class AuthorsServiceImplTest {

    private static final String AUTHORS_NAME = "name";
    private static final Book BOOK = new Book("id1", "name1", List.of(), List.of());
    private static final List<Book> AUTHOR_S_BOOKS = List.of(BOOK);
    private static final BooksResponse BOOKS_RESPONSE = new BooksResponse("id1", "name1", List.of(), List.of());
    private final static Flux<Book> BOOKS_RESPONSE_FLUX = Flux.fromIterable(AUTHOR_S_BOOKS);
    private static final List<BooksResponse> AUTHOR_S_BOOKS_RESPONSE = List.of(BOOKS_RESPONSE);


    @MockBean
    private BooksRepository repository;
    @MockBean
    private BookRequestMapper mapper;
    @Autowired
    private AuthorsService service;

    @Test
    @DisplayName("должен найти книги автора по имени")
    void findByAuthor() {
        when(repository.findAllByAuthorsContains(AUTHORS_NAME)).thenReturn(BOOKS_RESPONSE_FLUX);
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.findByAuthor(Mono.just(AUTHORS_NAME))
                .collectList()
                .block();

        Assertions.assertThat(result).hasSize(AUTHOR_S_BOOKS_RESPONSE.size())
                .contains(AUTHOR_S_BOOKS_RESPONSE.toArray(BooksResponse[]::new));
    }
}