package ru.otus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.entities.User;

public interface UsersRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);

}
