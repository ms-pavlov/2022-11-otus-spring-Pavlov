package ru.otus.api;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import ru.otus.openapi.api.KeyApiDelegate;
import ru.otus.openapi.model.KeyResponse;
import ru.otus.securities.KeyService;

import java.security.KeyPair;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeyApiDelegateImplTest {

    private final static KeyPair KEY_PAIR = Keys.keyPairFor(SignatureAlgorithm.RS512);

    @Mock
    private KeyService keyService;
    @Mock
    private ServerWebExchange exchange;
    private KeyApiDelegate delegate;

    @BeforeEach
    void setUp() {
        delegate = new KeyApiDelegateImpl(keyService);
    }

    @Test
    @DisplayName("Возвращает открытый ключ в зашифрованном виде")
    void getKey() {
        when(keyService.getPublic()).thenReturn(KEY_PAIR.getPublic());

        var result = delegate.getKey(exchange);

        verify(keyService, times(1)).getPublic();
        verify(keyService, times(0)).getPrivate();

        assertEquals(
                Base64.getEncoder().encodeToString(KEY_PAIR.getPublic().getEncoded()),
                Optional.ofNullable(result.block())
                        .map(ResponseEntity::getBody)
                        .map(KeyResponse::getKey)
                        .orElse(null)
        );
    }
}