package ru.otus.services;

import ru.otus.domains.IndicationDto;

import java.util.List;

public interface JsonListParserService {

    List<IndicationDto> parse(String json);
}
