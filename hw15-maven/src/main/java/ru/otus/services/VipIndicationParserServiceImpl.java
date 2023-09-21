package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.domains.IndicationDto;
import ru.otus.domains.VipIndicationEntity;

import java.util.Optional;

@Service
public class VipIndicationParserServiceImpl implements VipIndicationParserService{
    @Override
    public VipIndicationEntity parse(IndicationDto dto) {
        return VipIndicationEntity.builder()
                .value(Optional.ofNullable(dto)
                        .map(IndicationDto::getValue)
                        .orElse(null))
                .name(Optional.ofNullable(dto)
                        .map(IndicationDto::getName)
                        .orElse(null))
                .timestamp(Optional.ofNullable(dto)
                        .map(IndicationDto::getTimestamp)
                        .orElse(null))
                .source(Optional.ofNullable(dto)
                        .map(IndicationDto::getSource)
                        .orElse(null))
                .build();
    }
}
