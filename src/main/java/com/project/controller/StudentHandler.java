package com.project.controller;


import com.project.model.Student;
import com.project.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class StudentHandler {
    private final StudentService studentService;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(studentService.findAll(), Student.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        return studentService
                .findById(Integer.valueOf(request.pathVariable("id")))
                .flatMap(student -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(student))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findBySurname(ServerRequest request) {
        return request.queryParam("nazwisko")
                .map(nazwa -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(studentService.findByNazwa(nazwa), Student.class))
                .orElseGet(() -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request
                .bodyToMono(Student.class)
                .flatMap(studentService::create)
                .flatMap(s -> ServerResponse
                        .created(URI.create(String.format("/students/%d", s.getStudentId())))
                        .build());
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        int pathId = Integer.parseInt(request.pathVariable("id"));
        return request.bodyToMono(Student.class)
                .doOnNext(student -> student.setStudentId(pathId))
                .flatMap(studentService::update)
                .flatMap(student -> ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        return studentService
                .findById(Integer.valueOf(request.pathVariable("id")))
                .flatMap(student -> ServerResponse
                        .noContent()
                        .build(studentService.delete(student.getStudentId())))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteAll(ServerRequest request) {
        return studentService.deleteAll()
                .then(ServerResponse.noContent().build());
    }
}
