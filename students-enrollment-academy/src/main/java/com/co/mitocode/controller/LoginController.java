package com.co.mitocode.controller;


import com.co.mitocode.model.Students;
import com.co.mitocode.model.User;
import com.co.mitocode.security.AuthRequest;
import com.co.mitocode.security.AuthResponse;
import com.co.mitocode.security.ErrorLogin;
import com.co.mitocode.security.JWTUtil;
import com.co.mitocode.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;

//Clase S9
@RestController
@Slf4j
public class LoginController {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private IUserService service;

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest authRequest) {
        log.info("Entro al login -> {}", authRequest);
        return service.searchByUser(authRequest.getUsername())
                .map(userDetails -> {
                    log.info("Validando password -> {} , username -> {}", userDetails.getPassword(), userDetails.getUsername());
                    if (BCrypt.checkpw(authRequest.getPassword(), userDetails.getPassword())) {
                        String token = jwtUtil.generateToken(userDetails);
                        Date expiration = jwtUtil.getExpirationDateFromToken(token);

                        return ResponseEntity.ok(new AuthResponse(token, expiration));
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new ErrorLogin("Bad Credentials", new Date()));

                    }
                }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

    }

}
