package com.co.mitocode.controller;


import com.co.mitocode.dto.RequestSaveEnrollmentDTO;
import com.co.mitocode.model.Courses;
import com.co.mitocode.model.Enrollment;
import com.co.mitocode.model.Students;
import com.co.mitocode.repository.ICoursesRepo;
import com.co.mitocode.service.impl.CoursesServiceImpl;
import com.co.mitocode.service.impl.EnrollmentServiceImpl;
import com.co.mitocode.service.impl.StudentsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {

    @Autowired
    private EnrollmentServiceImpl enrollmentService;

    @Autowired
    private CoursesServiceImpl coursesService;

    @Autowired
    private StudentsServiceImpl studentsService;


    @Autowired
    private ICoursesRepo coursesRepo;


    @PostMapping
    public Mono<ResponseEntity<Enrollment>> save(@Valid @RequestBody RequestSaveEnrollmentDTO requestSaveEnrollmentDTO,
                                                 final ServerHttpRequest req){
        Mono<Students> studentsMono = studentsService.findById(requestSaveEnrollmentDTO.getIdStudent());
        Mono<List<Courses>> listCoursesMono = coursesRepo.findAllById(requestSaveEnrollmentDTO.getCourses()).collectList();

        return listCoursesMono.zipWith(studentsMono, (courses, student) -> Enrollment.builder()
                .coursesList(courses)
                .estudent(student)
                .status(requestSaveEnrollmentDTO.getStatus())
                .dateEnrollment(requestSaveEnrollmentDTO.getDateEnrollment())
                .build())
                .flatMap(enrollmentService::save)
                .map(e -> ResponseEntity
                        .created(URI.create(req.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e));
    }

}
