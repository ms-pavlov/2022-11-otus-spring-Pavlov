package ru.otus.securities.services;

import ru.otus.openapi.model.UserRequest;
import ru.otus.openapi.model.UserResponse;

import java.util.List;

public interface UsersService {

    UserResponse getUser(String username);

    UserResponse create(UserRequest user);

    Boolean existsByUsername(String username);

    List<UserResponse> getUsers();

    UserResponse update(String id, UserRequest request);
}
