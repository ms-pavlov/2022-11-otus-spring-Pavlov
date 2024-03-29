package ru.otus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.requests.AuthorsRequest;
import ru.otus.dto.responses.AuthorsResponse;
import ru.otus.entities.Author;
import ru.otus.mappers.AuthorsMapper;
import ru.otus.repositories.AuthorsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorsServiceImpl implements AuthorsService {

    private final AuthorsRepository repository;
    private final AuthorsMapper mapper;

    @Autowired
    public AuthorsServiceImpl(AuthorsRepository repository, AuthorsMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional //findByName без транзакции, save - открывает транзакцию по-умолчанию
    public AuthorsResponse create(AuthorsRequest request) {
        if(repository.findByName(request.getName()) != null) {
            throw new AuthorExistException();
        }
        return Optional.of(request)
                .map(mapper::create)
                .map(repository::save)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true) //findById без транзакции
    public AuthorsResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional //findByName, findById - без транзакции, save - открывает транзакцию по-умолчанию
    public AuthorsResponse update(Long id, AuthorsRequest request) {
        Optional.ofNullable(request)
                .map(AuthorsRequest::getName)
                .map(repository::findByName)
                .map(Author::getId)
                .ifPresent(value -> {
                    if(!value.equals(id)) {
                        throw new AuthorExistException();
                    }});
        return repository.findById(id)
                .map(author -> {
                    mapper.update(author, request);
                    return author;
                })
                .map(repository::save)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override // deleteById - открывает транзакцию по-умолчанию
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true) //findByName - без транзакции
    public AuthorsResponse findByName(String name) {
        return Optional.ofNullable(name)
                .map(repository::findByName)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true) //findAll - без транзакции
    public List<AuthorsResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
