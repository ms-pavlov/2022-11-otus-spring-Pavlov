package ru.otus.services;

import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;

import java.util.List;

public interface BooksService {

    BooksResponse create(BooksRequest request);

    BooksResponse findById(String id);

    BooksResponse update(String id, BooksRequest request);

    void delete(String id);

    List<BooksResponse> findByName(String name);

    List<BooksResponse> findAll();
}
