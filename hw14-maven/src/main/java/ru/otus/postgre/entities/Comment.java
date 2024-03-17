package ru.otus.postgre.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "commentsSeq")
    @SequenceGenerator(
            name = "commentsSeq",
            allocationSize = 1,
            sequenceName = "comments_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "comment")
    private String comment;

    @Fetch(FetchMode.SELECT)
    @ManyToOne
    @JoinColumn(name = "book")
    private Book book;

    public Comment(Long id, String comment) {
        this(id, comment, null);
    }
}
