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
import ru.otus.services.GenresService;

import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GenresControllerTest {

    private static final String GENRES_NAME = "name";
    private static final List<BooksResponse> GENRE_S_BOOKS = List.of(
            new BooksResponse("id1", "name1", List.of(), List.of())
    );
    private final static Flux<BooksResponse> BOOKS_RESPONSE_FLUX = Flux.fromIterable(GENRE_S_BOOKS);

    @LocalServerPort
    private int port;

    @MockBean
    private GenresService service;

    private WebClient webClient;

    @BeforeEach
    public void setUp() {
        webClient = WebClient.create(String.format("http://localhost:%d", port));
    }

    @Test
    @DisplayName("Поиск книг жанра по названию")
    void findByName() {
        Mockito.when(service.findByGenre(ArgumentMatchers.any())).thenReturn(BOOKS_RESPONSE_FLUX);

        List<BooksResponse> result = webClient
                .get()
                .uri("/api/v1/genre?name="+GENRES_NAME)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(BooksResponse.class)
                .take(GENRE_S_BOOKS.size())
                .timeout(Duration.ofSeconds(3))
                .collectList()
                .block();

        Assertions.assertThat(result).hasSize(GENRE_S_BOOKS.size())
                .contains(GENRE_S_BOOKS.toArray(BooksResponse[]::new));
    }


}