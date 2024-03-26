package ru.otus.securities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    private final static String TEST_USER_NAME = "test";
    private final static User TEST_USER = new User(TEST_USER_NAME, "");


    @Mock
    private Map<String, User> users;
    @Mock
    private UserDetails userDetail;
    @Mock
    private Function<User, UserDetails> userDetailsAdapter;

    private ReactiveUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new UsersServiceImpl(users, userDetailsAdapter);
    }

    @Test
    @DisplayName("Возвращает UserDetails по имени")
    void findByUsername() {
        when(users.get(TEST_USER_NAME)).thenReturn(TEST_USER);
        when(userDetailsAdapter.apply(TEST_USER)).thenReturn(userDetail);

        var result = userDetailsService.findByUsername(TEST_USER_NAME);

        assertEquals(userDetail, result.block());
        verify(users, times(1)).get(TEST_USER_NAME);
    }
}