package ru.otus.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Author implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    @Id
    private String id;

    private String name;

    @DocumentReference(lazy = true)
    private List<Book> books;

    public Author(String id, String name) {
        this(id, name, new ArrayList<>());
    }

    public Author() {
        this.books = new ArrayList<>();
    }
}
