package ru.otus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.domains.IndicationEntity;

import java.util.Date;
import java.util.function.Function;

@Service
public class IndicationConsumerServiceImpl implements IndicationConsumerService {

    private final Function<Date, String> dateFormat;

    @Autowired
    public IndicationConsumerServiceImpl(Function<Date, String> dateFormat) {
        this.dateFormat = dateFormat;
    }


    @Override
    public String consume(IndicationEntity entity) {
        return String.format(
                "Показания неважно откуда в %s - %s : %s%n",
                dateFormat.apply(entity.getTimestamp()),
                entity.getName(),
                entity.getValue());
    }
}
