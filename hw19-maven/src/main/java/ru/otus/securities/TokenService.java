package ru.otus.securities;

import io.jsonwebtoken.Claims;
import ru.otus.openapi.model.UserResponse;

public interface TokenService {

    String create(String scope, UserResponse user);

    Claims parse(String token);

}
