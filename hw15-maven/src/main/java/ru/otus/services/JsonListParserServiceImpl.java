package ru.otus.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.domains.IndicationDto;
import ru.otus.services.exceptions.UnknownListJsonFormatException;

import java.util.List;

@Service
public class JsonListParserServiceImpl implements JsonListParserService{

    private final ObjectMapper mapper;

    @Autowired
    public JsonListParserServiceImpl(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<IndicationDto> parse(String json) {
        try {
            return mapper.readValue(
                    json,
                    CollectionType.construct(List.class, SimpleType.construct(IndicationDto.class)));
        } catch (JsonProcessingException e) {
            throw new UnknownListJsonFormatException(e);
        }
    }
}
