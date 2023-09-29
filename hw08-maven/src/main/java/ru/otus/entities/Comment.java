package ru.otus.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    @Id
    private String id;

    private String comment;

    private Book book;

    public Comment(String id, String comment) {
        this(id, comment, null);
    }
}
