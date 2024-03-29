package ru.otus.securities;

import io.jsonwebtoken.Claims;
import ru.otus.model.entities.User;

public interface TokenService {

    String create(String scope, User user);

    Claims parse(String token);

}
