package ru.otus.mappers;

import ru.otus.model.dto.input.UserRequestDto;
import ru.otus.model.dto.output.UserResponseDto;
import ru.otus.model.entities.User;

public interface UserMapper {

    User create(UserRequestDto request);

    UserResponseDto toDto(User user);
}
