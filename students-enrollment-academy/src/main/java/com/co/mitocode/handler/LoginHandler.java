package com.co.mitocode.handler;

import com.co.mitocode.security.AuthRequest;
import com.co.mitocode.security.AuthResponse;
import com.co.mitocode.security.ErrorLogin;
import com.co.mitocode.security.JWTUtil;
import com.co.mitocode.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Date;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class LoginHandler {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private IUserService service;


    public Mono<ServerResponse> login(@RequestBody ServerRequest serverRequest) {
        Mono<AuthRequest> authRequestMono = serverRequest.bodyToMono(AuthRequest.class);

        return authRequestMono.flatMap(user -> service.searchByUser(user.getUsername())
                .flatMap(userDetails -> {
                            if (BCrypt.checkpw(user.getPassword(), userDetails.getPassword())) {
                                String token = jwtUtil.generateToken(userDetails);
                                Date expiration = jwtUtil.getExpirationDateFromToken(token);

                                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(new AuthResponse(token, expiration)));
                            } else {
                                return ServerResponse.status(HttpStatus.UNAUTHORIZED).body(fromValue(new ErrorLogin("Bad Credentials", new Date())));


                            }
                        }
                )).switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build());

//                service.searchByUser(authRequest.getUsername())
//                .map(userDetails -> {
//                    if (BCrypt.checkpw(authRequest.getPassword(), userDetails.getPassword())) {
//                        String token = jwtUtil.generateToken(userDetails);
//                        Date expiration = jwtUtil.getExpirationDateFromToken(token);
//
//                        return ResponseEntity.ok(new AuthResponse(token, expiration));
//                    } else {
//                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                                .body(new ErrorLogin("Bad Credentials", new Date()));
//
//                    }
//                }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

    }

}
