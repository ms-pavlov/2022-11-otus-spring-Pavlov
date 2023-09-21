package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.domains.VipIndicationEntity;

import java.util.Date;
import java.util.function.Function;

@Service
public class VipIndicationConsumerServiceImpl implements VipIndicationConsumerService{

    private final Function<Date, String> dateFormat;

    public VipIndicationConsumerServiceImpl(Function<Date, String> dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public String consume(VipIndicationEntity entity) {
        return String.format(
                "Важные показания от %s в %s - %s : %s%n",
                entity.getSource(),
                dateFormat.apply(entity.getTimestamp()),
                entity.getName(),
                entity.getValue());
    }
}
