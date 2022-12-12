package ru.otus.data.storages;

public class AnswersStorageImpl implements AnswersStorage{
    private final int[] answers;

    public AnswersStorageImpl(QuestionDataStorage questionDataStorage) {
        answers = getNewAnswers(questionDataStorage);
    }

    private int[] getNewAnswers(QuestionDataStorage questionDataStorage) {
        int[] result =  new int[questionDataStorage.getTemplate().size()];
        for (int i = 0; i < questionDataStorage.getTemplate().size(); i++) {
            result[i] = getRandomInteger(questionDataStorage.getTemplate().get(i));
        }
        return result;
    }

    private static int getRandomInteger(int max){
        return (int)(Math.random()*(max+1));
    }

    @Override
    public int[] getAnswers() {
        return answers;
    }
}
