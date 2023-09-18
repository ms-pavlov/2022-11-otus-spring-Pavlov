package ru.otus.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class VipIndicationEntity {
    private BigDecimal value;
    private String name;
    private Date timestamp;
    private String source;
}
