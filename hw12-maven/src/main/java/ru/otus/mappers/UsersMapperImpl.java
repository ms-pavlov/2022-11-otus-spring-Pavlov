package ru.otus.mappers;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.otus.dto.requests.UsersRequest;
import ru.otus.dto.responses.UsersResponse;
import ru.otus.entities.Authority;
import ru.otus.entities.User;
import ru.otus.repositories.AuthoritiesRepository;

import java.util.ArrayList;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UsersMapperImpl implements UsersMapper {

    private final AuthoritiesRepository authoritiesRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(UsersRequest request) {
        User result = new User();
        result.setName(request.getName());
        result.setLogin(request.getLogin());
        result.setPassword(passwordEncoder.encode(request.getPassword()));
        result.setAuthorities(
                authoritiesRepository.saveAll(
                        Optional.of(request)
                                .map(UsersRequest::getAccesses)
                                .orElseGet(ArrayList::new)
                                .stream()
                                .map(item -> Authority.builder()
                                        .access(item)
                                        .user(result)
                                        .build())
                                .toList()));
        return result;
    }

    @Override
    public UsersResponse toDto(User entity) {
        return UsersResponse.builder()
                .name(entity.getName())
                .login(entity.getLogin())
                .accesses(
                        Optional.of(entity)
                                .map(User::getAuthorities)
                                .orElseGet(ArrayList::new)
                                .stream()
                                .map(Authority::getAccess)
                                .toList())
                .build();
    }
}
