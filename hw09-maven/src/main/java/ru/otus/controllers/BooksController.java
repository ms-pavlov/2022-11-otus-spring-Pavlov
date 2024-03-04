package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.services.BooksService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BooksController {

    private final BooksService service;

    @PostMapping("/book/")
    public String create(BooksRequest request) {
        service.create(request);
        return "redirect:/";
    }

    @GetMapping("/book/{id}/comment")
    public String findById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("book", service.findById(id));
        return "books/book";
    }

    @GetMapping("/book/form")
    public String getForm() {
        return "books/create";
    }

    @GetMapping("/book/{id}/form")
    public String getEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("book", service.findById(id));
        return "books/edit";
    }

    @GetMapping("/")
    public String findPage(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            Model model) {
        Page<BooksResponse> books = service.findPage(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(20));
        model.addAttribute(
                "bookPage",
                books);
        model.addAttribute("books", books.getContent());
        return "index";
    }

    @PutMapping("/book/{id}")
    public String update(
            @PathVariable("id")Long id,
            BooksRequest request) {
        service.update(id, request);
        return String.format("forward:books/%d/book", id);
    }

    @DeleteMapping("/book/{id}")
    public String delete(@PathVariable("id") Long id) {
        service.delete(id);
        return "redirect:/";
    }
}
