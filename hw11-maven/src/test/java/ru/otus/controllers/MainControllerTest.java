package ru.otus.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.entities.Book;
import ru.otus.services.BooksService;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MainControllerTest {

    private static final String BOOKS_ID = "BOOKS_ID";
    private static final String AUTHOR_NAME = "AUTHOR_NAME";
    private static final String GENRE_NAME = "GENRE_NAME";
    private static final String BOOKS_NAME = "books_name";
    private final static Book BOOK = new Book(
            BOOKS_ID,
            BOOKS_NAME,
            List.of(),
            List.of());
    private final static BooksResponse BOOKS_RESPONSE = new BooksResponse(
            BOOK.getId(),
            BOOK.getName(),
            BOOK.getAuthors(),
            BOOK.getGenres());

    @MockBean
    private BooksService service;

    @LocalServerPort
    private int port;

    private WebClient webClient;

    @BeforeEach
    public void setUp() {
        webClient = WebClient.create(String.format("http://localhost:%d", port));
    }

    @Test
    @DisplayName("Стартовая страница")
    void index() {
        webClient
                .get()
                .uri("/")
                .accept(MediaType.TEXT_HTML)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .toBodilessEntity()
                .block();
    }

    @Test
    @DisplayName("Страница для отображения автора")
    void getAuthor() {
        webClient
                .get()
                .uri("/author?name="+ AUTHOR_NAME)
                .accept(MediaType.TEXT_HTML)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .toBodilessEntity()
                .block();
    }

    @Test
    @DisplayName("Страница для отображения жанра")
    void getGenre() {
        webClient
                .get()
                .uri("/genre?name="+ GENRE_NAME)
                .accept(MediaType.TEXT_HTML)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .toBodilessEntity()
                .block();
    }

    @Test
    @DisplayName("Страница для отображения книги")
    void getBook() {
        webClient
                .get()
                .uri("/book/"+ BOOKS_ID +"/comment")
                .accept(MediaType.TEXT_HTML)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .toBodilessEntity()
                .block();
    }

    @Test
    @DisplayName("Форма для создания книги")
    void createBook() {
        webClient
                .get()
                .uri("/book")
                .accept(MediaType.TEXT_HTML)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .toBodilessEntity()
                .block();
    }

    @Test
    @DisplayName("Форма для редактирования книги")
    void editBook() {
        Mockito.when(service.findById(ArgumentMatchers.eq(BOOKS_ID))).thenReturn(Mono.just(BOOKS_RESPONSE));

        webClient
                .get()
                .uri("/book/"+ BOOKS_ID)
                .accept(MediaType.TEXT_HTML)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .toBodilessEntity()
                .block();
    }
}