package ru.otus.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.services.AuthorsService;

import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorControllerTest {

    private static final String AUTHORS_NAME = "name";
    private static final List<BooksResponse> AUTHOR_S_BOOKS = List.of(
            new BooksResponse("id1", "name1", List.of(), List.of())
    );
    private final static Flux<BooksResponse> BOOKS_RESPONSE_FLUX = Flux.fromIterable(AUTHOR_S_BOOKS);

    @LocalServerPort
    private int port;

    @MockBean
    private AuthorsService service;

    private WebClient webClient;

    @BeforeEach
    public void setUp() {
        webClient = WebClient.create(String.format("http://localhost:%d", port));
    }

    @Test
    @DisplayName("Поиск книг автора по имени")
    void findByName() {
        Mockito.when(service.findByAuthor(ArgumentMatchers.any())).thenReturn(BOOKS_RESPONSE_FLUX);

        List<BooksResponse> result = webClient
                .get()
                .uri("/api/v1/author?name=" + AUTHORS_NAME)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(BooksResponse.class)
                .timeout(Duration.ofSeconds(3))
                .collectList()
                .block();

        Assertions.assertThat(result).hasSize(AUTHOR_S_BOOKS.size())
                .contains(AUTHOR_S_BOOKS.toArray(BooksResponse[]::new));
    }
}