package com.co.mitocode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Document(collection = "USERS")
public class User {

    @Id
    private String id;
    @Field("username")
    private String username;
    @Field("password")
    private String password;
    @Field("status")
    private Boolean status;
    @Field("roles")
    private List<Role> roles;
}
