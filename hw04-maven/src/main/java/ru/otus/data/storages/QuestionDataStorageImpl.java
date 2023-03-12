package ru.otus.data.storages;

import org.springframework.stereotype.Component;
import ru.otus.data.Question;
import ru.otus.data.sources.QuestionDataSource;

import java.util.List;
import java.util.Optional;

@Component
public class QuestionDataStorageImpl implements QuestionDataStorage {

    private final QuestionDataSource dataSource;

    public QuestionDataStorageImpl(QuestionDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Question find(int index) {
        return Optional.ofNullable(dataSource)
                .map(QuestionDataSource::getQuestions)
                .map(list -> list.get(index))
                .map(Question::new)
                .orElse(null);
    }

    @Override
    public int getCount() {
        return Optional.ofNullable(dataSource)
                .map(QuestionDataSource::getQuestions)
                .map(List::size)
                .orElse(0);
    }

    @Override
    public List<Question> findAll() {
        if (null != dataSource && null != dataSource.getQuestions()) {
            return dataSource.getQuestions().stream().map(Question::new).toList();
        }
        return null;
    }

    @Override
    public List<Integer> getTemplate() {
        return dataSource.getQuestions().stream()
                .map(question -> question.getQuestionDecisions().size())
                .toList();
    }
}
