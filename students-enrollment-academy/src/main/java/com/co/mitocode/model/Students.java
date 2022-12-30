package com.co.mitocode.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "ESTUDIANTES")
public class Students {

    @EqualsAndHashCode.Include
    @Id
    private String id;

    @Field(name = "NOMBRE")
    @NotNull
    @Size(min = 2)
    private String name;

    @Field(name = "APELLIDO")
    @NotNull
    @Size(min = 2)
    private String lastName;

    @Field(name = "DNI")
    @NotNull
    @Size(min = 7)
    private String DNI;

    @Field(name = "EDAD")
    @NotNull
    @Min(6)
    private Long age;

}
