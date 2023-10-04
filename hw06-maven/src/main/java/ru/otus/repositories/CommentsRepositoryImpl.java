package ru.otus.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import ru.otus.entities.Comment;

import java.util.List;
import java.util.Optional;

@Component
public class CommentsRepositoryImpl implements CommentsRepository{

    @PersistenceContext
    private final EntityManager em;

    public CommentsRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Comment> getCommentsByBookId(Long bookId) {
        try {
            return em.createQuery("SELECT c FROM Comment c JOIN FETCH c.book WHERE c.book.id = :bookId", Comment.class)
                    .setParameter("bookId", bookId)
                    .getResultList();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    @Override
    public Optional<Comment> getById(long id) {
        return Optional.of(id)
                .map(value -> em.find(Comment.class, value));
    }

    @Override
    public List<Comment> getAll() {
        return em.createQuery("SELECT c FROM Comment c JOIN FETCH c.book", Comment.class)
                .getResultList();
    }

    @Override
    public Comment create(Comment entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public Comment update(Comment entity) {
        return em.merge(entity);
    }

    @Override
    public void delete(Long id) {
        em.remove(em.find(Comment.class, id));
    }

    @Override
    public int count() {
        try {
            return em.createQuery("select count(c.id) from Comment c", Long.class)
                    .getSingleResult()
                    .intValue();
        } catch (NoResultException noResultException) {
            return 0;
        }
    }
}
