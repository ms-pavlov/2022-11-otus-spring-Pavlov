package ru.otus.securities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.otus.model.entities.User;

import java.util.Collection;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class UserDetailsAdapterTest {

    private final static String USER_NAME = "test1";
    private final static String PASSWORD = "test2";
    private final static User USER = new User(USER_NAME, PASSWORD);

    @Mock
    private Function<User, Collection<? extends GrantedAuthority>> authoritiesStrategy;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = new UserDetailsAdapter(USER, authoritiesStrategy);
    }

    @Test
    @DisplayName("getAuthorities вызывает стратегию authoritiesStrategy")
    void getAuthorities() {
        userDetails.getAuthorities();

        verify(authoritiesStrategy, times(1)).apply(USER);
    }

    @Test
    @DisplayName("getPassword возвращает значение password()")
    void getPassword() {
        assertEquals(PASSWORD, userDetails.getPassword());
    }

    @Test
    @DisplayName("getUsername возвращает значение username()")
    void getUsername() {
        assertEquals(USER_NAME, userDetails.getUsername());
    }

    @Test
    @DisplayName("isAccountNonExpired возвращает true")
    void isAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    @DisplayName("isAccountNonLocked возвращает true")
    void isAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    @DisplayName("isCredentialsNonExpired возвращает true")
    void isCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("isEnabled возвращает true")
    void isEnabled() {
        assertTrue(userDetails.isEnabled());
    }
}