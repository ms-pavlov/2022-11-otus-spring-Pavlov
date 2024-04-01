package ru.otus.securities.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.mappers.UserMapper;
import ru.otus.model.entities.User;
import ru.otus.openapi.model.UserRequest;
import ru.otus.openapi.model.UserResponse;
import ru.otus.repositories.UsersRepository;
import ru.otus.securities.AnonimusUD;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UserDetailsService, UsersService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;
    private final Function<User, UserDetails> userDetailsAdapter;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(username)
                .map(usersRepository::findByLogin)
                .map(userDetailsAdapter)
                .orElseGet(AnonimusUD::new);
    }

    @Override
    public UserResponse getUser(String username) {
        return userMapper.toDto(usersRepository.findByLogin(username));
    }

    @Override
    public UserResponse create(UserRequest user) {
        return userMapper.toDto(
                usersRepository.save(
                        userMapper.create(user)));
    }

    @Override
    public Boolean existsByUsername(String username) {
        return usersRepository.existsByLogin(username);
    }

    @Override
    public List<UserResponse> getUsers() {
        return usersRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserResponse update(String id, UserRequest request) {
        return usersRepository.findById(id)
                .map(user -> userMapper.update(user, request))
                .map(usersRepository::save)
                .map(userMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Не удалось сохранить пользователя"));
    }
}
