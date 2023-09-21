package ru.otus.services;

import ru.otus.domains.IndicationDto;
import ru.otus.domains.IndicationEntity;

public interface IndicationParserService {

    IndicationEntity parse(IndicationDto dto);
}
