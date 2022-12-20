package ru.otus.views;

import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.List;

@Component
public class QuestionFormatViewImpl implements QuestionFormatView{

    private final PrintStream printStream;

    public QuestionFormatViewImpl(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void show(QuestionDto question) {
        printStream.println(question.getText());
        var decisions = question.getDecisions();
        for (int i = 0; i < decisions.size(); i++) {
            printStream.println((i + 1) + ". " + decisions.get(i));
        }
        printStream.println();
    }

    @Override
    public void show(List<QuestionDto> questions) {
        questions.forEach(this::show);
    }
}
