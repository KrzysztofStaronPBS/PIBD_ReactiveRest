package com.project.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ProjectRouter {

    @Bean
    public RouterFunction<ServerResponse> projectsRoute(ProjectHandler projectHandler) {
        return RouterFunctions
                .route(GET("/api/projects").and(queryParam("nazwa", n -> !n.isBlank())), projectHandler::findByName)
                .andRoute(GET("/api/projects"), projectHandler::findAll)
                .andRoute(GET("/api/projects/{id}"), projectHandler::findById)
                .andRoute(POST("/api/projects").and(contentType(APPLICATION_JSON)), projectHandler::create)
                .andRoute(PUT("/api/projects/{id}").and(contentType(APPLICATION_JSON)), projectHandler::update)
                .andRoute(DELETE("/api/projects/all"), projectHandler::deleteAll)
                .andRoute(DELETE("/api/projects/{id}"), projectHandler::delete);
    }
}
