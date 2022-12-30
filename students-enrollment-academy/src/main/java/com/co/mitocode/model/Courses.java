package com.co.mitocode.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "CURSOS")
public class Courses {

    @EqualsAndHashCode.Include
    @Id
    private String id;

    @Field(name = "NOMBRE")
    @NotNull
    @Size(min = 2)
    private String name;

    @Field(name = "SIGLAS")
    @NotNull
    @Size(min = 1)
    private String acronym;

    @Field(name = "ESTADO")
    @NotNull
    private Boolean estado;
}
