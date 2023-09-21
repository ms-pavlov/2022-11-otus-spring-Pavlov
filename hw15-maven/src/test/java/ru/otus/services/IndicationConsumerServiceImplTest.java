package ru.otus.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.domains.IndicationEntity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        IndicationConsumerServiceImpl.class
})
class IndicationConsumerServiceImplTest {
    private final static String PARAM_NAME = "test_name";
    private final static BigDecimal VALUE = BigDecimal.TEN;
    private final static Date DATE = new Date();
    private final static String DATE_FORMAT = new SimpleDateFormat("dd MMMM yyyy hh:mm", Locale.getDefault())
            .format(DATE);
    private final static String FORMAT = "Показания неважно откуда в %s - %s : %s%n";

    private final static String MESSAGE = String.format(FORMAT, DATE_FORMAT, PARAM_NAME, VALUE.doubleValue());
    private final static IndicationEntity ENTITY = IndicationEntity.builder()
            .name(PARAM_NAME)
            .timestamp(DATE)
            .value(VALUE.doubleValue())
            .build();

    @MockBean
    private Function<Date, String> dateStringMapper;

    @Autowired
    private IndicationConsumerService service;

    @Test
    void consume() {
        when(dateStringMapper.apply(DATE)).thenReturn(DATE_FORMAT);

        var result = service.consume(ENTITY);

        assertEquals(MESSAGE, result);
    }
}