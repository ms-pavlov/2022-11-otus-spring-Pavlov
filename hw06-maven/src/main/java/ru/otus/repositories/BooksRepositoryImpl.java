package ru.otus.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.otus.entities.Book;

import java.util.List;
import java.util.Optional;

@Repository
public class BooksRepositoryImpl implements BooksRepository{

    @PersistenceContext
    private final EntityManager em;

    public BooksRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Book> getByName(String name) {
        return null;
    }

    @Override
    public List<Book> getByAuthor(String name) {
        return null;
    }

    @Override
    public List<Book> getByGenre(String name) {
        return null;
    }

    @Override
    public Optional<Book> getById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Book> getAll() {
        return null;
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
    public void delete(Long aLong) {

    }

    @Override
    public boolean exist(Long aLong) {
        return false;
    }

    @Override
    public int count() {
        return 0;
    }
}
