package ru.otus.securities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.openapi.model.UserResponse;
import ru.otus.securities.services.KeyService;
import ru.otus.securities.services.KeyServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenFactoryImplTest {
    private final static String TEST_SCOPE = "TEST_SCOPE";
    private final static List<String> ACCESSES = List.of(TEST_SCOPE);
    private final static UserResponse USER = new UserResponse()
            .name("test")
            .login("test")
            .accesses(ACCESSES.stream().map(UserResponse.AccessesEnum::valueOf).toList());
    private final static KeyService KEY_SERVICE = new KeyServiceImpl();

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenServiceImpl(KEY_SERVICE);
    }

    @Test
    @DisplayName("Создает токен подписанный закрытым ключем, который содержит имя пользователя, uuid игры и доступные к управлению в игре объекты")
    void create() {
        var result = tokenService.create(TEST_SCOPE, USER);

        assertDoesNotThrow(() -> {
            Claims body = (Claims) Jwts.parserBuilder()
                    .setSigningKey(KEY_SERVICE.getPrivate())
                    .build()
                    .parse(result).getBody();

            List<String> accesses = (List<String>) body.get("accesses");

            assertEquals(ACCESSES, accesses);
            assertEquals(TEST_SCOPE, body.get("scope", String.class));
            assertEquals(USER.getLogin(), body.getSubject());
        });
    }
}