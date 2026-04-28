package com.project.service;

import com.project.model.Projekt;
import com.project.model.Zadanie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProjectService {

    Flux<Projekt> findAll();
    Mono<Projekt> findById(Integer projektId);
    Flux<Projekt> findByNazwa(String nazwa);
    Mono<Projekt> create(Projekt projekt);
    Mono<Projekt> update(Projekt projekt);
    Mono<Void> addProjectWithTasks(Projekt projekt, List<Zadanie> zadanie);
    Mono<Void> delete(Integer projektId);
    Mono<Void> deleteProjectWithTasks(Integer projektId);
    Mono<Void> deleteAll();
}
