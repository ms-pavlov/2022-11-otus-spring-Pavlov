package ru.otus.securities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class AnonimusUDTest {

    private final static String USER_NAME = "anonymous";

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = new AnonimusUD();
    }

    @Test
    @DisplayName("getAuthorities возвращает null")
    void getAuthorities() {
        assertNull(userDetails.getAuthorities());
    }

    @Test
    @DisplayName("getPassword возвращает null")
    void getPassword() {
        assertNull(userDetails.getPassword());
    }

    @Test
    @DisplayName("getUsername возвращает \"anonymous\"")
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