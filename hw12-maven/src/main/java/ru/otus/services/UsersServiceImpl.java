package ru.otus.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.requests.UsersRequest;
import ru.otus.dto.responses.UsersResponse;
import ru.otus.entities.User;
import ru.otus.mappers.UsersMapper;
import ru.otus.repositories.UsersRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService {


    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    @Override
    public User getUser(String username) {
        return Optional.ofNullable(username)
                .map(usersRepository::findByLogin)
                .orElseThrow(() -> new RuntimeException("Не удалось найти пользователя"));
    }

    @Override
    @Transactional
    public UsersResponse create(UsersRequest request) {
        User user = Optional.of(request)
                .map(usersMapper::create)
                .orElseThrow(() -> new RuntimeException("Не удалось создать пользователя"));
        return usersMapper.toDto(usersRepository.save(user));
    }

    @Override
    public List<UsersResponse> findAll() {
        return usersRepository.findAll()
                .stream()
                .map(usersMapper::toDto)
                .toList();
    }

}
