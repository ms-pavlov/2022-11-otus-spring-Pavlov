package ru.otus.model.dto.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    private String name;
    private String login;
    private String password;
    private String passwordConfirmation;
    private List<String> accesses;
    private List<String> scopes;

    public UserRequestDto(String username, String password) {
        this(username, username, password, password, new ArrayList<>(), new ArrayList<>());
    }

    public UserRequestDto(String username, String password, List<String> accesses) {
        this(username, username, password, password, accesses, new ArrayList<>());
    }
}
