package com.co.mitocode.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestSaveEnrollmentDTO {

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT-5")
    @NotNull
    private LocalDateTime dateEnrollment;
    @NotNull
    private String idStudent;
    @NotNull
    private List<String> courses;
    @NotNull
    private Boolean status;

}
