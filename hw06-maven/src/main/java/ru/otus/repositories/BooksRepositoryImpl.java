package ru.otus.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import ru.otus.entities.Book;

import java.util.List;
import java.util.Optional;

@Component
public class BooksRepositoryImpl implements BooksRepository {

    @PersistenceContext
    private final EntityManager em;


    public BooksRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Book> getByName(String name) {
        return em.createQuery("SELECT b FROM Book b JOIN FETCH b.comments WHERE b.name = :name", Book.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public Optional<Book> getById(long id) {
        return Optional.of(id)
                .map(value -> em.find(
                        Book.class,
                        value));
    }

    @Override
    public List<Book> getAll() {
        return em.createQuery("SELECT b FROM Book b JOIN FETCH b.comments", Book.class)
                .getResultList();
    }

    @Override
    public Book create(Book entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public Book update(Book entity) {
        return em.merge(entity);
    }

    @Override
    public void delete(Long id) {
        em.remove(em.find(Book.class, id));
    }

    @Override
    public int count() {
        try {
            return em.createQuery("select count(b.id) from Book b", Long.class)
                    .getSingleResult()
                    .intValue();
        } catch (NoResultException noResultException) {
            return 0;
        }
    }
}
