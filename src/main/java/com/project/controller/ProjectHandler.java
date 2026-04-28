package com.project.controller;

import java.net.URI;
import com.project.model.Projekt;
import com.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProjectHandler {
    private final ProjectService projectService;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM) // odsyła kolejne wyniki, widoczne jeżeli dłużej trwa ich
              //  .contentType(MediaType.APPLICATION_JSON) // poczeka na zebranie wszystkich wyników i wtedy odeśle
                .body(projectService.findAll(), Projekt.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        return projectService
                .findById(Integer.valueOf(request.pathVariable("id")))
                .flatMap(projekt -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(projekt))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findByName(ServerRequest request) {
        return request.queryParam("nazwa")
                .map(nazwa -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(projectService.findByNazwa(nazwa), Projekt.class))
                .orElseGet(() -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request
                .bodyToMono(Projekt.class)
                .flatMap(projectService::create)
                .flatMap(p -> ServerResponse
                        .created(URI.create(String.format("/projects/%d", p.getProjektId())))
                        .build());
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        int pathId = Integer.parseInt(request.pathVariable("id"));
        return request.bodyToMono(Projekt.class)
                .doOnNext(projekt -> projekt.setProjektId(pathId))
                .flatMap(projectService::update)
                .flatMap(projekt -> ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        int projectId = Integer.parseInt(request.pathVariable("id"));

        return projectService.findById(projectId)
                .flatMap(projekt -> projectService.deleteProjectWithTasks(projectId)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteAll(ServerRequest request) {
        return projectService.deleteAll()
                .then(ServerResponse.noContent().build());
    }
}
