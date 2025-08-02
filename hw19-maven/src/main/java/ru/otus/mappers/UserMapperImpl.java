package ru.otus.mappers;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.otus.model.entities.User;
import ru.otus.openapi.model.UserRequest;
import ru.otus.openapi.model.UserResponse;

import java.util.ArrayList;
import java.util.Optional;


@Component
@AllArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(UserRequest request) {
        User result = new User();
        result.setName(request.getName());
        result.setLogin(request.getLogin());
        result.setPassword(passwordEncoder.encode(request.getPassword()));
        result.setAccesses(
                Optional.of(request)
                        .map(UserRequest::getAccesses)
                        .orElseGet(ArrayList::new)
                        .stream()
                        .map(UserRequest.AccessesEnum::getValue)
                        .toList());
        result.setScopes(request.getScopes());
        return result;
    }

    @Override
    public User update(User entity, UserRequest request) {
        entity.setName(request.getName());
        entity.setLogin(request.getLogin());
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setAccesses(
                Optional.of(request)
                        .map(UserRequest::getAccesses)
                        .orElseGet(ArrayList::new)
                        .stream()
                        .map(UserRequest.AccessesEnum::getValue)
                        .toList());
        entity.setScopes(request.getScopes());
        return entity;
    }

    @Override
    public UserResponse toDto(User entity) {
        return new UserResponse()
                .id(entity.getId())
                .name(entity.getName())
                .login(entity.getLogin())
                .accesses(
                        Optional.of(entity)
                                .map(User::getAccesses)
                                .orElseGet(ArrayList::new)
                                .stream()
                                .map(UserResponse.AccessesEnum::fromValue)
                                .toList()
                )
                .scopes(entity.getScopes());
    }

}
