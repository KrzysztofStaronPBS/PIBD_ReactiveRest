package com.project.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;

@Component
public class ZadanieRouter {

    @Bean
    public RouterFunction<ServerResponse> tasksRoute(ZadanieHandler zadanieHandler) {
        return RouterFunctions
                .route(GET("/api/tasks").and(queryParam("nazwa", n -> !n.isBlank())), zadanieHandler::findByName)
                .andRoute(GET("/api/tasks/{id}"), zadanieHandler::findById)
                .andRoute(GET("/api/tasks"), zadanieHandler::findAll)
                .andRoute(POST("/api/tasks").and(contentType(APPLICATION_JSON)), zadanieHandler::create)
                .andRoute(PUT("/api/tasks/{id}").and(contentType(APPLICATION_JSON)), zadanieHandler::update)
                .andRoute(DELETE("/api/tasks/all"), zadanieHandler::deleteAll)
                .andRoute(DELETE("/api/tasks/{id}"), zadanieHandler::delete);
    }
}
