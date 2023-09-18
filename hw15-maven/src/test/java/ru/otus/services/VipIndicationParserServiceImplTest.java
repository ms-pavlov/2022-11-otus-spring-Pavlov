package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.domains.IndicationDto;
import ru.otus.domains.VipIndicationEntity;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        VipIndicationParserServiceImpl.class
})
class VipIndicationParserServiceImplTest {

    private final static String PARAM_NAME = "test_name";
    private final static String SOURCE = "test_source";
    private final static BigDecimal VALUE = BigDecimal.TEN;
    private final static Date DATE = new Date();
    private final static IndicationDto DTO = IndicationDto.builder()
            .name(PARAM_NAME)
            .source(SOURCE)
            .timestamp(DATE)
            .value(VALUE)
            .build();

    @Autowired
    private VipIndicationParserService service;

    @Test
    @DisplayName("переводит IndicationDto в VipIndicationEntity")
    void parse() {
        VipIndicationEntity result =  service.parse(DTO);

        assertEquals(PARAM_NAME, result.getName());
        assertEquals(SOURCE, result.getSource());
        assertEquals(DATE, result.getTimestamp());
        assertEquals(VALUE, result.getValue());
    }
}