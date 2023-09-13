package ru.otus.services;

import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;

import java.util.List;

public interface BooksService {

    BooksResponse create(BooksRequest request);

    BooksResponse findById(Long id);

    BooksResponse update(Long id, BooksRequest request);

    void delete(Long id);

    List<BooksResponse> findByName(String name);

    List<BooksResponse> findAll();
}
