package ru.otus.services;

import ru.otus.domains.IndicationDto;

public interface JsonParserService {

    IndicationDto parse(String json);
}
