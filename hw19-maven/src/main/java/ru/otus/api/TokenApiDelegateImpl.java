package ru.otus.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.openapi.api.TokenApiDelegate;
import ru.otus.openapi.model.TokenResponse;
import ru.otus.securities.TokenService;
import ru.otus.securities.services.UsersService;

import java.security.Principal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenApiDelegateImpl implements TokenApiDelegate {

    private final TokenService tokenService;
    private final UsersService usersService;

    @Override
    public ResponseEntity<TokenResponse> getToken(String scope) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(authentication)
                .map(Principal::getName)
                .map(usersService::getUser)
                .map(user -> tokenService.create(scope, user))
                .map(token -> new TokenResponse().scope(scope).token(token))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
