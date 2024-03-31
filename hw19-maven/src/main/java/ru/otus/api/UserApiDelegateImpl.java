package ru.otus.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.otus.openapi.api.UserApiDelegate;
import ru.otus.openapi.model.UserRequest;
import ru.otus.openapi.model.UserResponse;
import ru.otus.securities.services.UsersService;

import java.util.List;

@Service
@AllArgsConstructor
public class UserApiDelegateImpl implements UserApiDelegate {

    private final UsersService usersService;

    @Override
    public ResponseEntity<UserResponse> createUser(UserRequest userRequest) {
        return ResponseEntity.ok(usersService.create(userRequest));
    }

    @Override
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(usersService.getUsers());
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(String id, UserRequest userRequest) {
        return ResponseEntity.ok(usersService.update(id, userRequest));
    }
}
