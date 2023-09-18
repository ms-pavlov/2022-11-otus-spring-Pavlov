package ru.otus.domains;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndicationDto {

    @JsonProperty(value = "vip", defaultValue = "false")
    private Boolean vip = false;
    @JsonProperty("value")
    private BigDecimal value;
    @JsonProperty("name")
    private String name;
    @JsonProperty("source")
    private String source;
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date timestamp;

}
