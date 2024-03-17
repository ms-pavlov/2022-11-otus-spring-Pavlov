package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.services.BooksService;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final BooksService booksService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/author")
    public String getAuthor(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "authors/index";
    }

    @GetMapping("/genre")
    public String getGenre(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "genres/index";
    }

    @GetMapping("/book/{id}/comment")
    public String getBook(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        return "books/book";
    }

    @GetMapping("/book")
    public String createBook() {
        return "books/create";
    }

    @GetMapping("/book/{id}")
    public String editBook(@PathVariable("id") String id, Model model) {
        model.addAttribute("book", booksService.findById(id));
        return "books/edit";
    }
}
