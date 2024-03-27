package ru.otus.model.dto.output;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private String id;
    private String name;
    private String login;
    private List<String> accesses;
    private List<String> scopes;
}
