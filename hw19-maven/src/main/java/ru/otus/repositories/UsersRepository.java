package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.model.entities.User;

public interface UsersRepository extends MongoRepository<User, String> {

    User findByLogin(String username);

    Boolean existsByLogin(String username);
}
