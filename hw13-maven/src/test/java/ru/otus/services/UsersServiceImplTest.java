package ru.otus.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dto.requests.UsersRequest;
import ru.otus.dto.responses.UsersResponse;
import ru.otus.entities.User;
import ru.otus.mappers.UsersMapper;
import ru.otus.repositories.UsersRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Сервис авторизации")
@SpringBootTest(classes = {
        UsersServiceImpl.class
})
class UsersServiceImplTest {

    private final static String USER_NAME = "USER_NAME";
    private final static UsersRequest USERS_REQUEST = UsersRequest.builder().build();
    private final static UsersResponse USERS_RESPONSE = UsersResponse.builder().build();
    private final static User USER = new User();

    @MockBean
    private UsersRepository usersRepository;
    @MockBean
    private UsersMapper usersMapper;

    @Autowired
    private UsersService service;

    @Test
    @DisplayName("Поиск пользователя по логину")
    void getUser() {
        Mockito.when(usersRepository.findByLogin(USER_NAME)).thenReturn(USER);

        User result = service.getUser(USER_NAME);

        assertEquals(USER, result);
    }

    @Test
    @DisplayName("Должен вернуть исключение если пользователь не найден")
    void getUserFail() {
        Mockito.when(usersRepository.findByLogin(USER_NAME)).thenReturn(null);

        assertThrows(
                RuntimeException.class,
                () -> service.getUser(USER_NAME),
                "Не удалось найти пользователя");

        Mockito.verify(usersRepository, Mockito.times(1)).findByLogin(USER_NAME);
    }

    @Test
    @DisplayName("Создание пользователя")
    void create() {
        Mockito.when(usersRepository.save(USER)).thenReturn(USER);
        Mockito.when(usersMapper.create(USERS_REQUEST)).thenReturn(USER);
        Mockito.when(usersMapper.toDto(USER)).thenReturn(USERS_RESPONSE);

        UsersResponse result = service.create(USERS_REQUEST);

        assertEquals(USERS_RESPONSE, result);
    }

    @Test
    @DisplayName("Если нельзя создать пользователя, вернется исключение")
    void createFail() {
        Mockito.when(usersMapper.create(USERS_REQUEST)).thenReturn(null);

        assertThrows(
                RuntimeException.class,
                () -> service.create(USERS_REQUEST),
                "Не удалось найти пользователя");

        Mockito.verify(usersMapper, Mockito.times(1)).create(USERS_REQUEST);
    }

    @Test
    void findAll() {
        Mockito.when(usersRepository.findAll()).thenReturn(List.of(USER));
        Mockito.when(usersMapper.toDto(USER)).thenReturn(USERS_RESPONSE);

        List<UsersResponse> result = service.findAll();

        Assertions.assertThat(result).contains(USERS_RESPONSE);
    }
}