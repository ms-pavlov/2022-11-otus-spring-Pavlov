package ru.otus.services;

import ru.otus.dto.requests.UsersRequest;
import ru.otus.dto.responses.UsersResponse;
import ru.otus.entities.User;

import java.util.List;

public interface UsersService {

    User getUser(String username);

    UsersResponse create(UsersRequest request);

    List<UsersResponse> findAll();

}
