package com.co.mitocode.handler;

import com.co.mitocode.dto.RequestSaveEnrollmentDTO;
import com.co.mitocode.model.Courses;
import com.co.mitocode.model.Enrollment;
import com.co.mitocode.model.Students;
import com.co.mitocode.repository.ICoursesRepo;
import com.co.mitocode.service.impl.CoursesServiceImpl;
import com.co.mitocode.service.impl.EnrollmentServiceImpl;
import com.co.mitocode.service.impl.StudentsServiceImpl;
import com.co.mitocode.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class EnrollmentHandler {

    @Autowired
    private EnrollmentServiceImpl enrollmentService;

    @Autowired
    private StudentsServiceImpl studentsService;

    @Autowired
    private ICoursesRepo coursesRepo;

    @Autowired
    private RequestValidator requestValidator;

    public Mono<ServerResponse> save(ServerRequest serverRequest){
        Mono<RequestSaveEnrollmentDTO> requestSaveEnrollmentDTO = serverRequest.bodyToMono(RequestSaveEnrollmentDTO.class);
        return  requestSaveEnrollmentDTO
                .flatMap(request -> studentsService.findById(request.getIdStudent())
                        .zipWith(coursesRepo.findAllById(request.getCourses())
                                .collectList(),(student,courses) -> Enrollment.builder()
                        .coursesList(courses)
                        .estudent(student)
                        .status(request.getStatus())
                        .dateEnrollment(request.getDateEnrollment())
                        .build())
                        .flatMap(requestValidator::validate)
                        .flatMap(enrollmentService::save)
                        .flatMap(client -> ServerResponse
                        .created(URI.create(serverRequest.uri().toString().concat(client.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(client))));
    }
}
