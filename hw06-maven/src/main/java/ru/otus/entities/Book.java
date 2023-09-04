package ru.otus.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(name = "avatars-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("authors"),
                @NamedAttributeNode("genres")
        })
public class Book implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "authorsSeq")
    @SequenceGenerator(
            name = "genresSeq",
            allocationSize = 1,
            sequenceName = "genres_seq")
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "books_authors",
            joinColumns = @JoinColumn(name = "books", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authors", referencedColumnName = "id"))
    private List<Author> authors;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "books_genres",
            joinColumns = @JoinColumn(name = "books", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genres", referencedColumnName = "id"))
    private List<Genre> genres;

}

