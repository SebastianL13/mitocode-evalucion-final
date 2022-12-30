package com.co.mitocode.controller;

import com.co.mitocode.model.Courses;
import com.co.mitocode.model.Students;
import com.co.mitocode.repository.IStudentsRepo;
import com.co.mitocode.service.impl.CoursesServiceImpl;
import com.co.mitocode.service.impl.StudentsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.Comparator;

@RestController
@RequestMapping("/students")
public class StudentsController {


    @Autowired
    private StudentsServiceImpl service;


    @GetMapping("/findAll/{orden}")
    public Mono<ResponseEntity<Flux<Students>>> findAll(@PathVariable Long orden){
        Flux<Students> studentsFlux = service.findAll().sort(
                orden == 0
                ? Comparator.comparing(Students::getAge)
                : Comparator.comparing(Students::getAge).reversed());

        return Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(studentsFlux))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Students>> findById(@PathVariable("id") String id) {
        return service.findById(id)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Students>> save(@Valid @RequestBody Students students, final ServerHttpRequest req) {
        return service.save(students)
                .map(e -> ResponseEntity
                        .created(URI.create(req.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Students>> update(@Valid @PathVariable("id") String id, @RequestBody Students students) {
        students.setId(id);

        Mono<Students> monoBody = Mono.just(students);
        Mono<Students> monoDB = service.findById(id);

        return monoDB.zipWith(monoBody, (db, c) -> {
                    db.setId(id);
                    db.setName(c.getName());
                    db.setAge(c.getAge());
                    db.setDNI(c.getDNI());
                    db.setLastName(c.getLastName());
                    return db;
                })
                .flatMap(service::update)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
        return service.findById(id)
                .flatMap(e -> service.delete(e.getId())
                        .thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
