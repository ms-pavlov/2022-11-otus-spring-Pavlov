package ru.otus.securities;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class UsersServiceImpl implements ReactiveUserDetailsService, UsersService {

    private final Map<String, User> users;
    private final Function<User, UserDetails> userDetailsAdapter;

    public UsersServiceImpl(
            Map<String, User> users,
            Function<User, UserDetails> userDetailsAdapter) {
        this.users = users;
        this.userDetailsAdapter = userDetailsAdapter;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.just(userDetailsAdapter.apply(getUser(username)));
    }

    @Override
    public User getUser(String username) {
        return Optional.ofNullable(username)
                .map(users::get)
                .orElseGet(() -> new User("anonymous", null));
    }
}
