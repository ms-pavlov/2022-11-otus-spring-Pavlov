package ru.otus.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentsRequest {
    private final String comment;
    private final Long bookId;
}
