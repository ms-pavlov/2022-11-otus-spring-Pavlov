package ru.otus.securities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import ru.otus.model.entities.User;
import ru.otus.securities.services.KeyService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
public class TokenServiceImpl implements TokenService {

    private final KeyService keyService;


    public TokenServiceImpl(KeyService keyService) {
        this.keyService = keyService;
    }

    @Override
    public String create(String scope, User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(accessExpiration)
                .signWith(keyService.getPrivate())
                .claim("accesses", user.getAccesses())
                .claim(
                        "scope",
                        Optional.ofNullable(scope)
                                .filter(user::hasAccess)
                                .orElseGet(user::getDefaultAccess)
                )
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
