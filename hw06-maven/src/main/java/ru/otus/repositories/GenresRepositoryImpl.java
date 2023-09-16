package ru.otus.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.otus.entities.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenresRepositoryImpl implements GenresRepository{

    @PersistenceContext
    private final EntityManager em;

    public GenresRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Genre getByName(String name) {
        try {
            return em.createQuery("SELECT g FROM Genre g WHERE g.name = :name", Genre.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    @Override
    public Optional<Genre> getById(long id) {
        return Optional.of(id)
                .map(value -> em.find(Genre.class, value));
    }

    @Override
    public List<Genre> getAll() {
        return em.createQuery("SELECT g FROM Genre g", Genre.class)
                .getResultList();
    }

    @Override
    public Genre create(Genre entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public Genre update(Genre entity) {
        entity.getBooks()
                .stream()
                .filter(book -> book.getId() == null)
                .forEach(em::persist);
        return em.merge(entity);
    }

    @Override
    public void delete(Long id) {
        em.remove(em.find(Genre.class, id));
    }

    @Override
    public boolean exist(Long id) {
        try {
            long count = em.createQuery("select count(g) from Genre g where g.id = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return count > 0;
        } catch (NoResultException noResultException) {
            return false;
        }
    }

    @Override
    public int count() {
        try {
            return em.createQuery("select count(g.id) from Genre g", Long.class)
                    .getSingleResult()
                    .intValue();
        } catch (NoResultException noResultException) {
            return 0;
        }
    }

    @Override
    public boolean existName(String name) {
        try {
            long count = em.createQuery("select count(g) from Genre g where g.name = :name", Long.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return count > 0;
        } catch (NoResultException noResultException) {
            return false;
        }
    }
}
