package com.project.service;

import com.project.model.Zadanie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ZadanieService {
    Flux<Zadanie> findAll();
    Mono<Zadanie> findById(Integer zadanieId);
    Flux<Zadanie> findByNazwa(String nazwa);
    Mono<Zadanie> create(Zadanie zadanie);
    Mono<Zadanie> update(Zadanie zadanie);
    Mono<Void> delete(Integer zadanieId);
    Mono<Void> deleteAll();
}
