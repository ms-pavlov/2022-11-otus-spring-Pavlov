package ru.otus.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.otus.openapi.api.KeyApiDelegate;
import ru.otus.openapi.model.KeyResponse;
import ru.otus.securities.services.KeyService;

import java.security.PublicKey;
import java.util.Base64;
import java.util.Optional;

@Service
public class KeyApiDelegateImpl implements KeyApiDelegate {

    private final KeyService keyService;

    public KeyApiDelegateImpl(KeyService keyService) {
        this.keyService = keyService;
    }

    @Override
    public ResponseEntity<KeyResponse> getKey() {
        return ResponseEntity.ok(
                new KeyResponse()
                        .key(
                                Optional.ofNullable(keyService.getPublic())
                                        .map(PublicKey::getEncoded)
                                        .map(key -> Base64.getEncoder().encodeToString(key))
                                        .orElse("")));
    }
}
