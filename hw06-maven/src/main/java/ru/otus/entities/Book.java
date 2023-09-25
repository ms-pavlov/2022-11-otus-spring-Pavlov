package ru.otus.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
public class Book implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "booksSeq")
    @SequenceGenerator(
            name = "booksSeq",
            allocationSize = 1,
            sequenceName = "books_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "books_authors",
            joinColumns = @JoinColumn(name = "books", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authors", referencedColumnName = "id"))
    private List<Author> authors;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "books_genres",
            joinColumns = @JoinColumn(name = "books", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genres", referencedColumnName = "id"))
    private List<Genre> genres;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.LAZY, cascade={CascadeType.ALL})
    @JoinColumn(name = "book")
    private List<Comment> comments;

    public Book() {
        this.authors = new ArrayList<>();
        this.genres = new ArrayList<>();
        this.comments = new ArrayList<>();
    }
}

