package ru.otus.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.model.enums.Expressions;

@Document(collection = "actions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderAction {

    @Id
    private String id;

    private Long actionId;
    private Expressions expression;
}
