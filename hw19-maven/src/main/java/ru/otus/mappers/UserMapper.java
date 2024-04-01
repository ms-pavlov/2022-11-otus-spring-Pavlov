package ru.otus.mappers;

import ru.otus.model.entities.User;
import ru.otus.openapi.model.UserRequest;
import ru.otus.openapi.model.UserResponse;

public interface UserMapper {

    User create(UserRequest request);

    User update(User entity, UserRequest request);

    UserResponse toDto(User user);
}
