package ru.otus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.entities.Authority;

public interface AuthoritiesRepository extends JpaRepository<Authority, Long> {
}
