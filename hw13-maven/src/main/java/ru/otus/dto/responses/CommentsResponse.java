package ru.otus.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentsResponse {
    private final Long id;
    private final String comment;
    private final String owner;
    private final String ownerLogin;

    @Override
    public String toString() {
        return "[" + id + "]: " + comment + "\n\r";
    }
}
