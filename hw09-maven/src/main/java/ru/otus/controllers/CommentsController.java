package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.services.CommentsService;

@Controller
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService service;

    @PostMapping("/comment")
    public String create(CommentsRequest request, Model model) {
        service.create(request);
        return String.format("redirect:/book/%d/comment", request.getBookId());
    }

    public void findById(Long id, Model model) {
        service.findById(id);
    }

    public void findByBook(Long bookId, Model model) {
        service.findByBookId(bookId);
    }

    public void update(Long id, CommentsRequest request, Model model) {
        service.update(id, request);
    }

    @DeleteMapping("/comment/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        service.delete(id);
        return "forward:/";
    }

    public void findAll(Model model) {
        service.findAll();
    }

}
