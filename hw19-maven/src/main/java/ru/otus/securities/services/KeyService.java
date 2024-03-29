package ru.otus.securities.services;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface KeyService {

    PublicKey getPublic();

    PrivateKey getPrivate();
}
