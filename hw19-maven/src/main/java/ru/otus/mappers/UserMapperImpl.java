package ru.otus.mappers;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.otus.model.dto.input.UserRequestDto;
import ru.otus.model.dto.output.UserResponseDto;
import ru.otus.model.entities.User;


@Component
@AllArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(UserRequestDto request) {
        User result = new User();
        result.setName(request.getName());
        result.setLogin(request.getLogin());
        result.setPassword(passwordEncoder.encode(request.getPassword()));
        result.setAccesses(request.getAccesses());
        result.setScopes(request.getScopes());
        return result;
    }

    @Override
    public UserResponseDto toDto(User entity) {
        return UserResponseDto.builder()
                .name(entity.getName())
                .login(entity.getLogin())
                .accesses(entity.getAccesses())
                .scopes(entity.getScopes())
                .build();
    }

}
