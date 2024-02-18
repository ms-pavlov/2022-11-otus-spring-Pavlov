package ru.otus.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ru.otus.dto.requests.AuthorsRequest;
import ru.otus.services.AuthorsService;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorsService service;

    public void create(AuthorsRequest request, Model model) {
        service.create(request);
    }

    public void findById(Long id, Model model) {
        service.findById(id);
    }

    public void findByName(String name, Model model) {
        service.findByName(name);
    }

    public void update(Long id, AuthorsRequest request, Model model) {
        service.update(id, request);
    }

    public void delete(Long id, Model model) {
        service.delete(id);
    }

    public void findAll(Model model) {
        service.findAll();
    }
}
