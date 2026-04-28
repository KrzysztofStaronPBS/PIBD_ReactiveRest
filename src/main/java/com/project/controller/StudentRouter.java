package com.project.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;

@Configuration
public class StudentRouter {

    @Bean
    public RouterFunction<ServerResponse> studentsRoute(StudentHandler studentHandler) {
        return RouterFunctions
                .route(GET("/api/students").and(queryParam("nazwisko", n -> !n.isBlank())), studentHandler::findBySurname)
                .andRoute(GET("/api/students"), studentHandler::findAll)
                .andRoute(GET("/api/students/{id}"), studentHandler::findById)
                .andRoute(POST("/api/students").and(contentType(APPLICATION_JSON)), studentHandler::create)
                .andRoute(PUT("/api/students/{id}").and(contentType(APPLICATION_JSON)), studentHandler::update)
                .andRoute(DELETE("/api/students/all"), studentHandler::deleteAll)
                .andRoute(DELETE("/api/students/{id}"), studentHandler::delete);
    }
}
