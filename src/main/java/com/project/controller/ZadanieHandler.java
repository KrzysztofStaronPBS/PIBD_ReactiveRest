package com.project.controller;

import com.project.model.Zadanie;
import com.project.service.ZadanieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class ZadanieHandler {
    private final ZadanieService zadanieService;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(zadanieService.findAll(), Zadanie.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        return zadanieService
                .findById(Integer.valueOf(request.pathVariable("id")))
                .flatMap(zadanie -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(zadanie))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findByName(ServerRequest request) {
        return request.queryParam("nazwa")
                .map(nazwa -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(zadanieService.findByNazwa(nazwa), Zadanie.class))
                .orElseGet(() -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request
                .bodyToMono(Zadanie.class)
                .flatMap(zadanieService::create)
                .flatMap(p -> ServerResponse
                        .created(URI.create(String.format("/tasks/%d", p.getZadanieId())))
                        .build());
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        int pathId = Integer.parseInt(request.pathVariable("id"));
        return request.bodyToMono(Zadanie.class)
                .doOnNext(zadanie -> zadanie.setZadanieId(pathId))
                .flatMap(zadanieService::update)
                .flatMap(zadanie -> ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        return zadanieService
                .findById(Integer.valueOf(request.pathVariable("id")))
                .flatMap(zadanie -> ServerResponse
                        .noContent()
                        .build(zadanieService.delete(zadanie.getZadanieId())))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteAll(ServerRequest request) {
        return zadanieService.deleteAll()
                .then(ServerResponse.noContent().build());
    }
}
