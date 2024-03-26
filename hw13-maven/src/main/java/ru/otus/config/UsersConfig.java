package ru.otus.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.otus.dto.requests.UsersRequest;
import ru.otus.entities.Authority;
import ru.otus.entities.User;
import ru.otus.models.UserDetailsAdapter;
import ru.otus.services.UsersService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Configuration
public class UsersConfig {


    @Bean
    public Function<User, Collection<? extends GrantedAuthority>> authoritiesStrategy() {
        return user -> Optional.of(user)
                .map(User::getAuthorities)
                .orElseGet(ArrayList::new)
                .stream()
                .map(Authority::getAccess)
                .map(authority -> (GrantedAuthority) () -> authority)
                .toList();
    }

    @Bean
    public Function<User, UserDetails> userDetailsAdapter(Function<User, Collection<? extends GrantedAuthority>> authoritiesStrategy) {
        return user -> new UserDetailsAdapter(user, authoritiesStrategy);
    }

    @Bean
    public CommandLineRunner CommandLineRunnerBean(UsersService usersService) {
        return (args) -> {
            addUser(usersService, "admin", "ADMIN");
            addUser(usersService, "user", "USER");
        };
    }

    private void addUser(UsersService usersService, String login, String access) {
        try {
            usersService.getUser(login);
        } catch (RuntimeException exception) {
            usersService.create(
                    UsersRequest.builder()
                            .name(login)
                            .login(login)
                            .password("password")
                            .accesses(List.of(access))
                            .build());
        }
    }
}
