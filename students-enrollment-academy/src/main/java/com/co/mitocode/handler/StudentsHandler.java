package com.co.mitocode.handler;

import com.co.mitocode.model.Courses;
import com.co.mitocode.model.Students;
import com.co.mitocode.service.impl.CoursesServiceImpl;
import com.co.mitocode.service.impl.StudentsServiceImpl;
import com.co.mitocode.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class StudentsHandler {

    @Autowired
    private RequestValidator requestValidator;
    @Autowired
    private StudentsServiceImpl studentsService;

    public Mono<ServerResponse> findAll(ServerRequest serverRequest){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentsService.findAll(), Courses.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");
        return studentsService.findById(id)
                .flatMap(students -> ServerResponse
                        .ok()
                        .body(fromValue(students)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }


    public Mono<ServerResponse> save(ServerRequest serverRequest){
        Mono<Students> monoStudents = serverRequest.bodyToMono(Students.class);
        return monoStudents
                .flatMap(requestValidator::validate)
                .flatMap(studentsService::save)
                .flatMap(student -> ServerResponse
                        .created(URI.create(serverRequest.uri().toString().concat(student.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(student)));
    }

    public Mono<ServerResponse> update(ServerRequest req){
        String id = req.pathVariable("id");

        Mono<Students> monoStudents = req.bodyToMono(Students.class);
        Mono<Students> monoDB = studentsService.findById(id);

        return monoDB
                .zipWith(monoStudents, (db, st) -> {
                    db.setId(id);
                    db.setName(st.getName());
                    db.setAge(st.getAge());
                    db.setLastName(st.getLastName());
                    db.setDNI(st.getDNI());
                    return db;
                })
                .flatMap(requestValidator::validate)
                .flatMap(studentsService::update)
                .flatMap(students -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(students))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest req){
        String id = req.pathVariable("id");

        return studentsService.findById(id)
                .flatMap(students -> studentsService.delete(students.getId())
                        .then(ServerResponse.noContent().build())
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
