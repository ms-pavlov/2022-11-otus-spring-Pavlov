package ru.otus.securities;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface KeyService {

    PublicKey getPublic();

    PrivateKey getPrivate();
}
