package ru.otus.controllers;

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
import reactor.core.publisher.Mono;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.services.CommentsService;

import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentsControllerTest {

    private final static String TEST_COMMENT = "TEST_COMMENT";
    private final static String TEST_COMMENT_ID = "1L";
    private final static String TEST_BOOK_ID = "1L";
    private final static String TEST_BOOK_NAME = "books_name";
    private final static CommentsRequest TEST_COMMENT_REQUEST = new CommentsRequest(TEST_COMMENT, TEST_BOOK_ID);
    private final static Mono<CommentsRequest> TEST_COMMENT_REQUEST_MONO = Mono.just(TEST_COMMENT_REQUEST);
    private final static CommentsResponse COMMENT_RESPONSE = new CommentsResponse(
            TEST_COMMENT_ID,
            TEST_COMMENT,
            new BooksResponse(
                    TEST_BOOK_ID,
                    TEST_BOOK_NAME,
                    List.of(),
                    List.of()));

    @MockBean
    private CommentsService service;

    @LocalServerPort
    private int port;

    private WebClient webClient;

    @BeforeEach
    public void setUp() {
        webClient = WebClient.create(String.format("http://localhost:%d", port));
    }

    @Test
    @DisplayName("Добавление комментария")
    void create() {
        Mockito.when(service.create(ArgumentMatchers.any())).thenReturn(Mono.just(COMMENT_RESPONSE));

        webClient
                .post()
                .uri("/api/v1/comment")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .body(TEST_COMMENT_REQUEST_MONO, CommentsRequest.class)
                .retrieve()
                .bodyToMono(CommentsResponse.class)
                .timeout(Duration.ofSeconds(3))
                .block();

        Mockito.verify(service, Mockito.times(1)).create(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Удаление комментария")
    void delete() {
        Mockito.when(service.delete(ArgumentMatchers.any())).thenReturn(Mono.empty());

        webClient
                .delete()
                .uri("/api/v1/comment/" + TEST_COMMENT_ID)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(Duration.ofSeconds(3))
                .block();

        Mockito.verify(service, Mockito.times(1)).delete(ArgumentMatchers.any());
    }


}