package ru.otus.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class IndicationEntity {

    private Double value;
    private String name;
    private Date timestamp;
}
