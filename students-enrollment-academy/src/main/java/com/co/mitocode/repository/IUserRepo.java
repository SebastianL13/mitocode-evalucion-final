package com.co.mitocode.repository;

import com.co.mitocode.model.User;
import reactor.core.publisher.Mono;

public interface IUserRepo extends IGenericRepo<User, String>{

    //@Query("{username: ?}")
    //DerivedQueries
    Mono<User> findOneByUsername(String username);
}
