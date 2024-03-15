package ru.otus.mappers;

import ru.otus.mongo.entities.Comment;

public interface CommentsMapper {

    Comment create(ru.otus.postgre.entities.Comment request);
}
