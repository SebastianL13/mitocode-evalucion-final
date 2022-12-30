package com.co.mitocode.handler;

import com.co.mitocode.model.Courses;
import com.co.mitocode.service.impl.CoursesServiceImpl;
import com.co.mitocode.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
@Component
public class CoursesHandler {

    @Autowired
    private RequestValidator requestValidator;
    @Autowired
    private CoursesServiceImpl coursesService;

    public Mono<ServerResponse> findAll(ServerRequest serverRequest){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(coursesService.findAll(), Courses.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");
        return coursesService.findById(id)
                .flatMap(courses -> ServerResponse
                        .ok()
                        .body(fromValue(courses)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }


    public Mono<ServerResponse> save(ServerRequest serverRequest){
        Mono<Courses> monoClient = serverRequest.bodyToMono(Courses.class);
        return monoClient
                .flatMap(requestValidator::validate)
                .flatMap(coursesService::save)
                .flatMap(client -> ServerResponse
                        .created(URI.create(serverRequest.uri().toString().concat(client.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(client)));


    }

    public Mono<ServerResponse> update(ServerRequest req){
        String id = req.pathVariable("id");

        Mono<Courses> monoCourses = req.bodyToMono(Courses.class);
        Mono<Courses> monoDB = coursesService.findById(id);

        return monoDB
                .zipWith(monoCourses, (db, co)-> {
                    db.setId(id);
                    db.setName(co.getName());
                    db.setAcronym(co.getAcronym());
                    db.setEstado(co.getEstado());
                    return db;
                })
                .flatMap(requestValidator::validate)
                .flatMap(coursesService::update)
                .flatMap(courses -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(courses))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest req){
        String id = req.pathVariable("id");

        return coursesService.findById(id)
                .flatMap(course -> coursesService.delete(course.getId())
                        .then(ServerResponse.noContent().build())
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
