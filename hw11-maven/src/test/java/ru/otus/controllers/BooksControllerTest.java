package ru.otus.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.entities.Book;
import ru.otus.services.BooksService;

import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BooksControllerTest {

    private static final String BOOKS_ID = "BOOKS_ID";
    private static final Integer PAGE = 1;
    private static final Integer SIZE = 1;
    private static final String BOOKS_NAME = "books_name";
    private final static Book BOOK = new Book(
            BOOKS_ID,
            BOOKS_NAME,
            List.of("author"),
            List.of("genre"));
    private final static BooksResponse BOOKS_RESPONSE = new BooksResponse(
            BOOK.getId(),
            BOOK.getName(),
            BOOK.getAuthors(),
            BOOK.getGenres());
    private final static Mono<BooksResponse> BOOKS_RESPONSE_MONO = Mono.just(BOOKS_RESPONSE);
    private final static BooksRequest BOOKS_REQUEST = new BooksRequest(BOOK.getName(), List.of("author"), List.of("genre"));
    private final static Mono<BooksRequest> BOOKS_REQUEST_MONO = Mono.just(BOOKS_REQUEST);
    private final static Page<BooksResponse> BOOKS_RESPONSE_PAGE = Page.empty();

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
    @DisplayName("Создание")
    void create() {
        Mockito.when(service.create(ArgumentMatchers.any())).thenReturn(BOOKS_RESPONSE_MONO);

        BooksResponse result = webClient
                .post()
                .uri("/api/v1/book/")
                .accept(MediaType.APPLICATION_JSON)
                .body(BOOKS_REQUEST_MONO, BooksRequest.class)
                .retrieve()
                .bodyToMono(BooksResponse.class)
                .timeout(Duration.ofSeconds(3))
                .block();

        Assertions.assertEquals(BOOKS_RESPONSE, result);

        Mockito.verify(service, Mockito.times(1)).create(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Получение книги по id")
    void findById() {
        Mockito.when(service.findById(ArgumentMatchers.eq(BOOKS_ID))).thenReturn(BOOKS_RESPONSE_MONO);

        BooksResponse result = webClient
                .get()
                .uri("/api/v1/book/" + BOOKS_ID)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(BooksResponse.class)
                .block();

        Assertions.assertEquals(BOOKS_RESPONSE, result);
    }

    @Test
    @DisplayName("Постраничный вывод")
    void findPage() {
        Mockito.when(service.findPage(Mono.just(PageRequest.of(PAGE, SIZE)))).thenReturn(Mono.just(BOOKS_RESPONSE_PAGE));

        webClient
                .get()
                .uri("/api/v1/book/?page=" + PAGE + "&size=" + SIZE)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .toBodilessEntity()
                .block();
    }

    @Test
    @DisplayName("Редактирование")
    void update() {
        Mockito.when(service.update(ArgumentMatchers.eq(BOOKS_ID), ArgumentMatchers.any())).thenReturn(BOOKS_RESPONSE_MONO);

        webClient
                .put()
                .uri("/api/v1/book/" + BOOKS_ID)
                .accept(MediaType.APPLICATION_JSON)
                .body(BOOKS_REQUEST_MONO, BooksRequest.class)
                .retrieve()
                .bodyToMono(BooksResponse.class)
                .block();

        Mockito.verify(service, Mockito.times(1)).update(ArgumentMatchers.eq(BOOKS_ID), ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Удаление")
    void delete() {
        webClient
                .delete()
                .uri("/api/v1/book/" + BOOKS_ID)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(Duration.ofSeconds(3))
                .block();

        Mockito.verify(service, Mockito.times(1)).delete(BOOKS_ID);
    }
}