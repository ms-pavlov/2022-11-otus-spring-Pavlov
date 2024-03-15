package ru.otus.mappers;


import ru.otus.mongo.entities.Book;

public interface BookRequestMapper {

    Book create(ru.otus.postgre.entities.Book request);

}
