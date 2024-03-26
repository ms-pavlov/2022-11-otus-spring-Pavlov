package ru.otus.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.dto.requests.UsersRequest;
import ru.otus.entities.Authority;
import ru.otus.entities.User;
import ru.otus.repositories.AuthoritiesRepository;

import java.util.List;

@DisplayName("Mapper для работы с пользователем должен")
@SpringBootTest(classes = {
        UsersMapperImpl.class
})
class UsersMapperImplTest {

    private final static String USER_NAME = "USER_NAME";
    private final static String LOGIN = "LOGIN";
    private final static String PASSWORD = "PASSWORD";
    private final static List<String> ACCESSES = List.of("ACCESS");
    private final static UsersRequest USERS_REQUEST = UsersRequest.builder()
            .name(USER_NAME)
            .login(LOGIN)
            .password(PASSWORD)
            .accesses(ACCESSES)
            .build();
    private final static List<Authority> AUTHORITIES = ACCESSES.stream()
            .map(item -> new Authority(null, item, null))
            .toList();
    private final static User USER = new User(
            USER_NAME,
            LOGIN,
            PASSWORD,
            AUTHORITIES);

    @MockBean
    private AuthoritiesRepository authoritiesRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersMapper usersMapper;

    @Test
    @DisplayName("создавать пользователя")
    void create() {
        Mockito.when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        Mockito.when(authoritiesRepository.saveAll(ArgumentMatchers.any())).thenReturn(AUTHORITIES);

        var result = usersMapper.create(USERS_REQUEST);

        Assertions.assertEquals(USER_NAME, result.getName());
        Assertions.assertEquals(LOGIN, result.getLogin());
        Assertions.assertEquals(PASSWORD, result.getPassword());
        Assertions.assertEquals(AUTHORITIES, result.getAuthorities());

    }

    @Test
    @DisplayName("преобразовывать в DTO")
    void toDto() {
        var result = usersMapper.toDto(USER);

        Assertions.assertEquals(USER_NAME, result.getName());
        Assertions.assertEquals(LOGIN, result.getLogin());
        Assertions.assertTrue(result.getAccesses().containsAll(ACCESSES));
    }
}