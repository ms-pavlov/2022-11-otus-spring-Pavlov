package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.services.CommentsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService service;

    @PostMapping("/api/v1/comment")
    public void create(
            CommentsRequest request,
            Authentication authentication) {
        service.create(request, authentication);
    }

    @DeleteMapping("/api/v1/comment/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }

    @GetMapping("/api/v1/comment")
    public List<CommentsResponse> getAll() {
        return service.getAll();
    }
}
