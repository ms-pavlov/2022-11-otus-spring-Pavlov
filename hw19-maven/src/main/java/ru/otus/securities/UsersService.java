package ru.otus.securities;

import reactor.core.publisher.Mono;
import ru.otus.model.dto.input.UserRequestDto;
import ru.otus.model.dto.output.UserResponseDto;
import ru.otus.model.entities.User;

public interface UsersService {

    Mono<User> getUser(String username);

    Mono<UserResponseDto> create(UserRequestDto user);

    Mono<Boolean> existsByUsername(String username);
}
