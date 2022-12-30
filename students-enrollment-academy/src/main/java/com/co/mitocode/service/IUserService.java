package com.co.mitocode.service;


import com.co.mitocode.model.User;
import reactor.core.publisher.Mono;

public interface IUserService extends ICRUD<User, String>{

    Mono<User> saveHash(User user);
    Mono<com.co.mitocode.security.User> searchByUser(String username);
}
