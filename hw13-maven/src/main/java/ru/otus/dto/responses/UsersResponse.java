package ru.otus.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UsersResponse {

    private final String name;
    private final String login;
    private final List<String> accesses;
}
