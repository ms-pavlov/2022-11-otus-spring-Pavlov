package ru.otus.services;

import ru.otus.domains.IndicationDto;
import ru.otus.domains.VipIndicationEntity;

public interface VipIndicationParserService {

    VipIndicationEntity parse(IndicationDto dto);
}
