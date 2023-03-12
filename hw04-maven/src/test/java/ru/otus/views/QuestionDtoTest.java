package ru.otus.views;

import org.junit.jupiter.api.Test;
import ru.otus.data.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionDtoTest {
    private final static List<String> VARIANTS = List.of("=3", "=5");
    private final static Question QUESTIONS_LIST =  new Question("2 x 2 = ?", VARIANTS, "=4");
    private final static int CORRECT_POSITION = 1;
    private final static int BAD_POSITION = 100;
    private final static int ANSWERS_COUNT = VARIANTS.size();

    @Test
    void checkAnswer() {
        QuestionDto test = new QuestionDto(QUESTIONS_LIST, CORRECT_POSITION);

        assertTrue(test.checkAnswer(CORRECT_POSITION));
    }

    @Test
    void checkAnswerWithBadPosition() {
        QuestionDto test = new QuestionDto(QUESTIONS_LIST, BAD_POSITION);

        assertTrue(test.checkAnswer(ANSWERS_COUNT));
    }

}