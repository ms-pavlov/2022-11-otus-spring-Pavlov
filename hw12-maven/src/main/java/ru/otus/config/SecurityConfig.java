package ru.otus.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.otus.dto.requests.UsersRequest;
import ru.otus.entities.Authority;
import ru.otus.entities.User;
import ru.otus.models.AnonimusUD;
import ru.otus.models.UserDetailsAdapter;
import ru.otus.services.UsersService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@EnableWebSecurity
@Configuration
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(16);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        (authorize) -> authorize
                                .requestMatchers("/**").authenticated()
//                                .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .anonymous(anonymous -> anonymous.principal(new AnonimusUD()))
                .rememberMe(rememberMe -> rememberMe.key("AnySecret").tokenValiditySeconds(60 * 30))
                .logout((logout) ->
                        logout.deleteCookies("remove")
                                .invalidateHttpSession(false));
        return http.build();
    }


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
            try {
                usersService.getUser("admin");
            } catch (RuntimeException exception) {
                usersService.create(
                        UsersRequest.builder()
                                .name("admin")
                                .login("admin")
                                .password("password")
                                .accesses(List.of("ADMIN"))
                                .build());
            }
        };
    }

}
