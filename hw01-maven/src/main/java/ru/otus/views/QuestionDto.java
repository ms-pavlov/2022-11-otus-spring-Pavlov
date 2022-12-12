package ru.otus.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.data.Question;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class QuestionDto {
    private String text;
    private List<String> decisions;
    private int correct;

    public QuestionDto(Question question, int position) {
        correct = prepareCorrect(position, question.getQuestionDecisions().size());
        text = question.getQuestionText();
        decisions = new ArrayList<>(question.getQuestionDecisions());
        decisions.add(correct, question.getQuestionCorrect());
    }

    private int prepareCorrect(int position, int size) {
        if (position > size) {
            return size;
        }
        return Math.max(position, 0);
    }
}
