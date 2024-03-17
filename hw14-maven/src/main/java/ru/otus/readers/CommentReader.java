package ru.otus.readers;

import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.otus.postgre.entities.Comment;
import ru.otus.postgre.repositories.CommentsRepository;

@Component
public class CommentReader extends AbstractPagingItemReader<Comment> {

    private final CommentsRepository commentsRepository;

    public CommentReader(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    @Override
    protected void doReadPage() {
        this.results = commentsRepository.findAll(PageRequest.of(getPage(), getPageSize()))
                .getContent();
    }
}
