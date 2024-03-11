package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.services.CommentsService;

@RestController
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService service;

    @GetMapping("/api/v1/comment")
    public Flux<CommentsResponse> getCommentsByBook(@RequestParam("book") String book) {
        return service.getCommentsByBook(Mono.just(book));
    }

    @PostMapping("/api/v1/comment")
    public Mono<Void> create(Mono<CommentsRequest> request) {
        return service.create(request).then();
    }

    @DeleteMapping("/api/v1/comment/{id}")
    public Mono<Void> delete(@PathVariable("id") String id) {
        return service.delete(Mono.just(id));
    }


}
