package ru.otus.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.otus.model.entities.Scope;
import ru.otus.model.enums.ScopePackages;
import ru.otus.openapi.model.ScopeRequest;
import ru.otus.openapi.model.ScopeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ScopeMapper {

    @Mappings({
            @Mapping(target = "packages", expression = "java(mapPackages(request))")
    })
    Scope create(ScopeRequest request);

    ScopeResponse toDto(Scope user);

    default List<String> mapPackages(ScopeRequest request) {
        return Optional.ofNullable(request)
                .map(ScopeRequest::getPackages)
                .orElseGet(ArrayList::new)
                .stream()
                .filter(ScopePackages::contains)
                .toList();
    }
}
