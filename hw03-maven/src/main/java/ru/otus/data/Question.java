package ru.otus.data;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @CsvBindByName(column = "questionText")
    private String questionText;

    @CsvBindAndSplitByName(column = "questionDecisions", elementType = String.class)
    private List<String> questionDecisions;

    @CsvBindByName(column = "questionCorrect")
    private String questionCorrect;

    public Question(Question question) {
        questionText = question.getQuestionText();
        questionCorrect = question.getQuestionCorrect();
        questionDecisions = new ArrayList<>(question.getQuestionDecisions());
    }
}
