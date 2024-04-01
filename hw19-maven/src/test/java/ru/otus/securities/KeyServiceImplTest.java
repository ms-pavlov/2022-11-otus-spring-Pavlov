package ru.otus.securities;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.securities.services.KeyService;
import ru.otus.securities.services.KeyServiceImpl;

import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class KeyServiceImplTest {

    private final static KeyPair KEY_PAIR = Keys.keyPairFor(SignatureAlgorithm.RS512);

    private KeyService service;


    @BeforeEach
    void setUp() {
        service = new KeyServiceImpl(KEY_PAIR);
    }

    @Test
    @DisplayName("Возвращает публичный ключ")
    void getPublic() {
        var result = service.getPublic();

        assertEquals(KEY_PAIR.getPublic(), result);
    }

    @Test
    @DisplayName("Возвращает приватный ключ")
    void getPrivate() {
        var result = service.getPrivate();

        assertEquals(KEY_PAIR.getPrivate(), result);
    }

    @Test
    @DisplayName("Есть конструктор по-умолчанию")
    void defaultConstructor() {
        assertDoesNotThrow(() -> new KeyServiceImpl());
    }
}