package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.domains.IndicationDto;
import ru.otus.domains.IndicationEntity;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {
        IndicationParserServiceImpl.class
})
class IndicationParserServiceImplTest {

    private final static String PARAM_NAME = "test_name";
    private final static BigDecimal VALUE = BigDecimal.TEN;
    private final static Date DATE = new Date();
    private final static IndicationDto DTO = IndicationDto.builder()
            .name(PARAM_NAME)
            .timestamp(DATE)
            .value(VALUE)
            .build();

    @Autowired
    private IndicationParserService service;

    @Test
    @DisplayName("переводит IndicationDto в IndicationEntity")
    void parse() {
        IndicationEntity result =  service.parse(DTO);

        assertEquals(PARAM_NAME, result.getName());
        assertEquals(DATE, result.getTimestamp());
        assertEquals(VALUE.doubleValue(), result.getValue());
    }
}