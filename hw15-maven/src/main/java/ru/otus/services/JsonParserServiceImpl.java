package ru.otus.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.domains.IndicationDto;
import ru.otus.services.exceptions.UnknownSingleJsonFormatException;

@Service
public class JsonParserServiceImpl implements JsonParserService{

    private final ObjectMapper mapper;

    @Autowired
    public JsonParserServiceImpl(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public IndicationDto parse(String json) {
        try {
            return mapper.readValue(json, IndicationDto.class);
        } catch (JsonProcessingException e) {
            throw new UnknownSingleJsonFormatException(e);
        }
    }
}
