package ru.otus.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.entities.User;
import ru.otus.models.AnonimusUD;
import ru.otus.repositories.UsersRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final Function<User, UserDetails> userDetailsAdapter;
    private final UsersRepository usersRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(username)
                .map(usersRepository::findByLogin)
                .map(userDetailsAdapter)
                .orElseGet(AnonimusUD::new);
    }
}
