package ru.otus.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "authorities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authority implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "authoritiesSeq")
    @SequenceGenerator(
            name = "authoritiesSeq",
            allocationSize = 1,
            sequenceName = "authorities_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "access")
    private String access;

    @Fetch(FetchMode.SELECT)
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

}
