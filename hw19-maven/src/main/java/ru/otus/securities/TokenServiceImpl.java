package ru.otus.securities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.openapi.model.UserResponse;
import ru.otus.securities.services.KeyService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final KeyService keyService;

    @Override
    public String create(String scope, UserResponse user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        String scopeName = Optional.ofNullable(scope)
                .filter(value -> user.getScopes().contains(scope))
                .orElse("main");
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(accessExpiration)
                .signWith(keyService.getPrivate())
                .claim("accesses", user.getAccesses())
                .claim("scope", scopeName)
                .compact();
    }

    @Override
    public Claims parse(String token) {
        var jwt = Jwts.parserBuilder()
                .setSigningKey(keyService.getPublic())
                .build()
                .parse(token);

        return (Claims) jwt.getBody();
    }
}
