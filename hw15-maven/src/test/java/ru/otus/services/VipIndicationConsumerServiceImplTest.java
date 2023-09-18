package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.domains.VipIndicationEntity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class VipIndicationConsumerServiceImplTest {

    private final static String PARAM_NAME = "test_name";
    private final static String SOURCE = "test_source";
    private final static BigDecimal VALUE = BigDecimal.TEN;
    private final static Date DATE = new Date();
    private final static String DATE_FORMAT = new SimpleDateFormat("dd MMMM yyyy hh:mm", Locale.getDefault())
            .format(DATE);
    private final static String FORMAT = "Важные показания от %s в %s - %s : %s%n";

    private final static String MESSAGE = String.format(FORMAT, SOURCE, DATE_FORMAT, PARAM_NAME, VALUE);
    private final static VipIndicationEntity ENTITY = VipIndicationEntity.builder()
            .name(PARAM_NAME)
            .source(SOURCE)
            .timestamp(DATE)
            .value(VALUE)
            .build();



    @Autowired
    private VipIndicationConsumerService service;

    @Test
    @DisplayName("формирует ответ из VipIndicationEntity")
    void consume() {
        var result = service.consume(ENTITY);

        assertEquals(MESSAGE, result);
    }
}