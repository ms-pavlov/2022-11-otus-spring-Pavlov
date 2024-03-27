package ru.otus.securities;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import ru.otus.model.entities.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
public class TokenFactoryImpl implements TokenFactory {

    private final KeyService keyService;


    public TokenFactoryImpl(KeyService keyService) {
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
}
