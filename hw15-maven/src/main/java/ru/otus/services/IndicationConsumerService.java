package ru.otus.services;

import ru.otus.domains.IndicationEntity;

public interface IndicationConsumerService {

    String consume(IndicationEntity entity);
}
