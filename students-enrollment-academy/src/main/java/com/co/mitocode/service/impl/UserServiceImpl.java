package com.co.mitocode.service.impl;

import com.co.mitocode.model.User;
import com.co.mitocode.repository.IGenericRepo;
import com.co.mitocode.repository.IRoleRepo;
import com.co.mitocode.repository.IUserRepo;
import com.co.mitocode.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl extends CRUDImpl<User, String> implements IUserService {

    @Autowired
    private IUserRepo repo;

    @Autowired
    private IRoleRepo rolRepo;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    @Override
    protected IGenericRepo<User, String> getRepo() {
        return repo;
    }

    @Override
    public Mono<User> saveHash(User user) {
        user.setPassword(bcrypt.encode(user.getPassword()));
        return repo.save(user);
    }

    @Override
    public Mono<com.co.mitocode.security.User> searchByUser(String username) {
        log.info("Validando User -> {}",username);
        Mono<User> monoUser = repo.findOneByUsername(username);
        List<String> roles = new ArrayList<>();

        return monoUser.flatMap(u -> {
            log.info("User -> {}",u.getUsername());
            return Flux.fromIterable(u.getRoles())
                    .flatMap(rol -> {
                        return rolRepo.findById(rol.getId())
                                .map(r -> {
                                    roles.add(r.getName());
                                    return r;
                                });
                    }).collectList().flatMap(list -> {
                        u.setRoles(list);
                        return Mono.just(u);
                    });
        }).flatMap(u -> Mono.just(new com.co.mitocode.security.User(u.getUsername(), u.getPassword(), u.getStatus(), roles)));
    }
}
