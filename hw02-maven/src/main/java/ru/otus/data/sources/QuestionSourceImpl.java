package ru.otus.data.sources;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Component;
import ru.otus.data.Question;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class QuestionSourceImpl implements QuestionDataSource {

    private final String resourcePath;

    public QuestionSourceImpl(Map<String,String> properties) {
        this.resourcePath = properties.get("path");
    }

    @Override
    public List<Question> getQuestions() {
        CsvToBean<Question> csvQuestions = new CsvToBeanBuilder<Question>(getResourceReader(resourcePath))
                .withType(Question.class)
                .withQuoteChar('\'')
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .build();

        return csvQuestions.stream().toList();
    }

    private Reader getResourceReader(String resourcePath) {
        return Optional.ofNullable(resourcePath)
                .filter(path -> !path.isEmpty())
                .map(path -> QuestionSourceImpl.class.getClassLoader().getResourceAsStream(resourcePath))
                .map(InputStreamReader::new)
                .orElseThrow(() -> new RuntimeException("No such file " + resourcePath));
    }
}
