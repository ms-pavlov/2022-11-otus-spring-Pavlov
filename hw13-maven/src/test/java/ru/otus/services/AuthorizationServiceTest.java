package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.entities.User;
import ru.otus.models.AnonimusUD;
import ru.otus.models.UserDetailsAdapter;
import ru.otus.repositories.UsersRepository;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Сервис авторизации")
@SpringBootTest(classes = {
        AuthorizationService.class
})
class AuthorizationServiceTest {

    private final static String USER_NAME = "USER_NAME";
    private final static User USER = new User();
    private final static UserDetails USER_DETAILS = new UserDetailsAdapter(USER, user -> null);
    private final static UserDetails ANONIMUS = new AnonimusUD();

    @MockBean
    private Function<User, UserDetails> userDetailsAdapter;
    @MockBean
    private UsersRepository usersRepository;
    @Autowired
    private UserDetailsService service;

    @Test
    @DisplayName("должен искать пользователя по login")
    void loadUserByUsername() {
        Mockito.when(usersRepository.findByLogin(USER_NAME)).thenReturn(USER);
        Mockito.when(userDetailsAdapter.apply(USER)).thenReturn(USER_DETAILS);

        UserDetails result = service.loadUserByUsername(USER_NAME);

        assertEquals(USER_DETAILS, result);
    }

    @Test
    @DisplayName("должен вернуть анонима если пользователь не найден")
    void notLoadUserByUsername() {
        Mockito.when(usersRepository.findByLogin(USER_NAME)).thenReturn(null);

        UserDetails result = service.loadUserByUsername(USER_NAME);

        assertEquals(ANONIMUS.getUsername(), result.getUsername());
    }
}