package ru.otus.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import ru.otus.entities.Author;

import java.util.List;
import java.util.Optional;

@Component
public class AuthorsRepositoryImpl implements AuthorsRepository {

    @PersistenceContext
    private final EntityManager em;

    public AuthorsRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Author getByName(String name) {
        try {
            return em.createQuery("SELECT a FROM Author a WHERE a.name = :name", Author.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    @Override
    public Optional<Author> getById(long id) {
        return Optional.of(id)
                .map(value -> em.find(Author.class, value));
    }

    @Override
    public List<Author> getAll() {
        return em.createQuery("SELECT a FROM Author a", Author.class)
                .getResultList();
    }

    @Override
    public Author create(Author entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public Author update(Author entity) {
        return em.merge(entity);
    }

    @Override
    public void delete(Long id) {
        em.remove(em.find(Author.class, id));
    }

    @Override
    public int count() {
        try {
            return em.createQuery("select count(a.id) from Author a", Long.class)
                    .getSingleResult()
                    .intValue();
        } catch (NoResultException noResultException) {
            return 0;
        }
    }

}
