package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.domains.IndicationDto;
import ru.otus.domains.IndicationEntity;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class IndicationParserServiceImpl implements IndicationParserService{

    @Override
    public IndicationEntity parse(IndicationDto dto) {
        return IndicationEntity.builder()
                .value(Optional.ofNullable(dto)
                        .map(IndicationDto::getValue)
                        .map(BigDecimal::doubleValue)
                        .orElse(null))
                .name(Optional.ofNullable(dto)
                        .map(IndicationDto::getName)
                        .orElse(null))
                .timestamp(Optional.ofNullable(dto)
                        .map(IndicationDto::getTimestamp)
                        .orElse(null))
                .build();
    }
}
