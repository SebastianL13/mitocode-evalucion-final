package com.co.mitocode.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "MATRICULAS")
public class Enrollment {
    @EqualsAndHashCode.Include
    @Id
    private String id;

    @Field(name = "FECHA_MATRICULA")
    @NotNull
    private LocalDateTime dateEnrollment;

    @Field(name = "ESTUDIANTE")
    @NotNull
    private Students estudent;

    @Field(name = "CURSOS")
    @NotNull
    @Size(min = 1)
    private List<Courses> coursesList;

    @Field(name = "STATUS")
    @NotNull
    private Boolean status;



}
