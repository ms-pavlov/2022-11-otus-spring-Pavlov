package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.services.BooksService;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final BooksService booksService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/author/{id}")
    public String getAuthor(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        return "authors/index";
    }

    @GetMapping("/genre/{id}")
    public String getGenre(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        return "genres/index";
    }

    @GetMapping("/book/{id}/comment")
    public String getBook(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        return "books/book";
    }

    @GetMapping("/book/form")
    public String createBook() {
        return "books/create";
    }

    @GetMapping("/book/{id}/form")
    public String editBook(@PathVariable("id") String id, Model model) {
        model.addAttribute("book", booksService.findById(id));
        return "books/edit";
    }
}
