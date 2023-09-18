package ru.otus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.domains.IndicationEntity;

import java.text.DateFormat;

@Service
public class IndicationConsumerServiceImpl implements IndicationConsumerService {

    private final DateFormat dateFormat;

    @Autowired
    public IndicationConsumerServiceImpl(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }


    @Override
    public String consume(IndicationEntity entity) {
        return String.format(
                "Показания неважно откуда в %s - %s : %s%n",
                dateFormat.format(entity.getTimestamp()),
                entity.getName(),
                entity.getValue());
    }
}
