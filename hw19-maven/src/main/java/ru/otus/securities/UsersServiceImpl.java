package ru.otus.securities;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.otus.mappers.UserMapper;
import ru.otus.model.dto.input.UserRequestDto;
import ru.otus.model.dto.output.UserResponseDto;
import ru.otus.model.entities.User;
import ru.otus.repositories.UsersRepository;

import java.util.function.Function;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements ReactiveUserDetailsService, UsersService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;
    private final Function<User, UserDetails> userDetailsAdapter;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return getUser(username)
                .map(userDetailsAdapter)
                .onErrorResume(
                        throwable -> Mono.just(new AnonimusUD()));
    }

    @Override
    public Mono<User> getUser(String username) {
        return usersRepository.findByLogin(username);
    }

    @Override
    public Mono<UserResponseDto> create(UserRequestDto user) {
        return usersRepository.save(userMapper.create(user))
                .map(userMapper::toDto);
    }

    @Override
    public Mono<Boolean> existsByUsername(String username) {
        return usersRepository.existsByLogin(username);
    }
}
