package ru.otus.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UsersRequest {

    private final String name;
    private final String login;
    private final String password;
    private final List<String> accesses;
}
