package ru.otus.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.domains.IndicationDto;
import ru.otus.services.exceptions.UnknownListJsonFormatException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {
        JsonListParserServiceImpl.class
})
class JsonListParserServiceImplTest {

    private final static String JSON = "[" +
            "{\"vip\":true,\"source\":\"Вася\",\"value\": 0.1, \"name\": \"температура\", \"timestamp\": \"2023-09-18 10:00\"}," +
            "{\"vip\":false,\"value\": 0.1, \"name\": \"температура\", \"timestamp\": \"2023-09-18 10:00\"} ]";

    @MockBean
    private ObjectMapper mapper;

    @Autowired
    private JsonListParserService service;

    @Test
    @DisplayName("Преобразует json в List<IndicationDto>")
    void parse() throws JsonProcessingException {
        service.parse(JSON);

        verify(mapper, times(1)).readValue(
                JSON,
                CollectionType.construct(List.class, SimpleType.construct(IndicationDto.class)));
    }

    @Test
    @DisplayName("в случае JsonProcessingException оборачивает его в UnknownListJsonFormatException")
    void parseJsonProcessingException() throws JsonProcessingException {
        when(mapper.readValue(
                JSON,
                CollectionType.construct(List.class, SimpleType.construct(IndicationDto.class))))
                .thenThrow(new JsonProcessingException(JSON) {});

        assertThrowsExactly(UnknownListJsonFormatException.class, ()->service.parse(JSON));
    }
}