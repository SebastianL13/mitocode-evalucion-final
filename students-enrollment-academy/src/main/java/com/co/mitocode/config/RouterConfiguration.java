package com.co.mitocode.config;


import com.co.mitocode.handler.CoursesHandler;
import com.co.mitocode.handler.EnrollmentHandler;
import com.co.mitocode.handler.LoginHandler;
import com.co.mitocode.handler.StudentsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfiguration {


    @Bean
    public RouterFunction<ServerResponse> routerCourses(CoursesHandler coursesHandler){
        return route(GET("/v2/courses"), coursesHandler::findAll)
                .andRoute(GET("/v2/courses/{id}"), coursesHandler::findById)
                .andRoute(POST("/v2/courses/"), coursesHandler::save)
                .andRoute(PUT("/v2/courses/{id}"), coursesHandler::update)
                .andRoute(DELETE("/v2/courses/{id}"), coursesHandler::delete);

    }

    @Bean
    public RouterFunction<ServerResponse> routerStudents(StudentsHandler studentsHandler){
        return route(GET("/v2/students"), studentsHandler::findAll)
                .andRoute(GET("/v2/students/{id}"), studentsHandler::findById)
                .andRoute(POST("/v2/students/"), studentsHandler::save)
                .andRoute(PUT("/v2/students/{id}"), studentsHandler::update)
                .andRoute(DELETE("/v2/students/{id}"), studentsHandler::delete);

    }

    @Bean
    public RouterFunction<ServerResponse> routerEnrollment(EnrollmentHandler enrollmentHandler){
        return route(POST("/v2/enrollment/"), enrollmentHandler::save);

    }

    @Bean
    public RouterFunction<ServerResponse> routerLogin(LoginHandler loginHandler){
        return route(POST("/v2/login"), loginHandler::login);

    }

}