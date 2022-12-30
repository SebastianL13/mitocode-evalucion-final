package com.co.mitocode.security;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthRequest {

    private String username;
    private String password;
}
