package ru.otus.securities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenFactoryImplTest {
    private final static String TEST_SCOPE = "TEST_SCOPE";
    private final static List<String> ACCESSES = List.of(TEST_SCOPE);
    private final static User USER = new User(
            "test",
            "pass",
            ACCESSES);
    private final static KeyService KEY_SERVICE = new KeyServiceImpl();

    private TokenFactory tokenFactory;

    @BeforeEach
    void setUp() {
        tokenFactory = new TokenFactoryImpl(KEY_SERVICE);
    }

    @Test
    @DisplayName("Создает токен подписанный закрытым ключем, который содержит имя пользователя, uuid игры и доступные к управлению в игре объекты")
    void create() {
        var result = tokenFactory.create(TEST_SCOPE, USER);

        assertDoesNotThrow(() -> {
            Claims body = (Claims) Jwts.parserBuilder()
                    .setSigningKey(KEY_SERVICE.getPrivate())
                    .build()
                    .parse(result).getBody();

            List<String> accesses = (List<String>) body.get("accesses");

            assertEquals(ACCESSES, accesses);
            assertEquals(TEST_SCOPE, body.get("scope", String.class));
            assertEquals(USER.username(), body.getSubject());
        });
    }
}