package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import ru.otus.model.entities.User;
import ru.otus.securities.AnonimusUD;
import ru.otus.securities.UserDetailsAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(16);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf().disable()
                .authorizeExchange(
                        authorize -> authorize
                                .pathMatchers("/api-docs/**", "/message/**").permitAll()
                                .pathMatchers("/", "/games/**").authenticated()
                                .anyExchange().permitAll()
                )
                .anonymous().principal(new AnonimusUD())
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .and()
                .logout();
        return http.build();
    }


    @Bean
    public Function<User, Collection<? extends GrantedAuthority>> authoritiesStrategy() {
        return user -> Optional.ofNullable(user)
                .map(User::getAccesses)
                .orElseGet(ArrayList::new)
                .stream()
                .map(access -> (GrantedAuthority) () -> access)
                .toList();
    }

    @Bean
    public Function<User, UserDetails> userDetailsAdapter(Function<User, Collection<? extends GrantedAuthority>> authoritiesStrategy) {
        return user -> new UserDetailsAdapter(user, authoritiesStrategy);
    }

}
