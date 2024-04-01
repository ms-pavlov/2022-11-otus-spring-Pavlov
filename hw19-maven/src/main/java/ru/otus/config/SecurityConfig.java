package ru.otus.config;

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
import ru.otus.model.entities.User;
import ru.otus.securities.AnonimusUD;
import ru.otus.securities.UserDetailsAdapter;

import java.util.ArrayList;
import java.util.Collection;
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
