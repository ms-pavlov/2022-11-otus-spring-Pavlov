package ru.otus.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.domains.IndicationDto;
import ru.otus.services.exceptions.UnknownSingleJsonFormatException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {
        JsonParserServiceImpl.class
})
class JsonParserServiceImplTest {

    private final static String JSON = "{\"vip\":false,\"value\": 0.1, \"name\": \"температура\", \"timestamp\": \"2023-09-18 10:00\"}";

    @MockBean
    private ObjectMapper mapper;

    @Autowired
    private JsonParserService service;

    @Test
    @DisplayName("Преобразует json в IndicationDto")
    void parse() throws JsonProcessingException {
        service.parse(JSON);

        verify(mapper, times(1)).readValue(JSON, IndicationDto.class);
    }

    @Test
    @DisplayName("в случае JsonProcessingException оборачивает его в UnknownSingleJsonFormatException")
    void parseJsonProcessingException() throws JsonProcessingException {
        when(mapper.readValue(JSON, IndicationDto.class)).thenThrow(new JsonProcessingException(JSON) {});

        assertThrowsExactly(UnknownSingleJsonFormatException.class, ()->service.parse(JSON));
    }
}