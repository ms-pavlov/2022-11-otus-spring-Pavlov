package ru.otus.mappers;

import ru.otus.dto.requests.UsersRequest;
import ru.otus.dto.responses.UsersResponse;
import ru.otus.entities.User;

public interface UsersMapper {

    User create(UsersRequest request);


    UsersResponse toDto(User entity);
}
