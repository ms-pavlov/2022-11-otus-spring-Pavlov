package ru.otus.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Author implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "authorsSeq")
    @SequenceGenerator(
            name = "authorsSeq",
            allocationSize = 1,
            sequenceName = "authors_seq")
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 100)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "books_authors",
            joinColumns = @JoinColumn(name = "authors", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "books", referencedColumnName = "id"))
    private List<Book> books;

    public Author(Long id, String name) {
        this(id, name, new ArrayList<>());
    }
}
