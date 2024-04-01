package ru.otus.securities.services;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@Service
public class KeyServiceImpl implements KeyService {

    private final KeyPair keyPair;

    public KeyServiceImpl() {
        this(Keys.keyPairFor(SignatureAlgorithm.RS512));
    }

    public KeyServiceImpl(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    @Override
    public PublicKey getPublic() {
        return keyPair.getPublic();
    }

    @Override
    public PrivateKey getPrivate() {
        return keyPair.getPrivate();
    }
}
