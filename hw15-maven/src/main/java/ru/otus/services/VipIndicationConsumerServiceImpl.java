package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.domains.VipIndicationEntity;

import java.text.DateFormat;

@Service
public class VipIndicationConsumerServiceImpl implements VipIndicationConsumerService{

    private final DateFormat dateFormat;

    public VipIndicationConsumerServiceImpl(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public String consume(VipIndicationEntity entity) {
        return String.format(
                "Важные показания от %s в %s - %s : %s%n",
                entity.getSource(),
                dateFormat.format(entity.getTimestamp()),
                entity.getName(),
                entity.getValue());
    }
}
