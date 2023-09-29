package ru.otus.services;

import ru.otus.dto.requests.AuthorsRequest;
import ru.otus.dto.responses.AuthorsResponse;

import java.util.List;

public interface AuthorsService {

    AuthorsResponse create(AuthorsRequest request);

    AuthorsResponse findById(String id);

    AuthorsResponse update(String id, AuthorsRequest request);

    void delete(String id);

    AuthorsResponse findByName(String name);

    List<AuthorsResponse> findAll();
}
