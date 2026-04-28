package com.project.client;

import java.time.LocalDate;

import org.springframework.web.reactive.function.client.WebClient;

import com.project.model.Projekt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class ReactiveWebClientApp {
    private final WebClient webClient;

    public static void main(String[] args) {
        new ReactiveWebClientApp(WebClient.create("http://localhost:8080/api/")).test();
    }

    public void test() {
        create(Projekt.builder()
                .nazwa("Projekt testowy")
                .opis("Opis testowy projektu")
                .dataOddania(LocalDate.of(2026, 4, 4))
                .build())
            .doOnNext( p ->
                    log.info("Id: {}, Nazwa: {}", p.getProjektId(), p.getNazwa()))
            .block();
    }

    public Flux<Projekt> findAll() {
        return webClient.get()
                .uri("/projects")
                .retrieve()
                .bodyToFlux(Projekt.class);
    }

    public Mono<Projekt> create(Projekt projekt) {
        return webClient.post()
                .uri("/projects")
                .bodyValue(projekt)
                .retrieve()
                .bodyToMono(Projekt.class);
    }

    public Mono<Projekt> findById(Integer projektId) {
        return webClient.get()
                .uri("/projects/{id}", projektId)
                .retrieve()
                .bodyToMono(Projekt.class);
    }

    public Mono<Projekt> update(Projekt projekt) {
        return webClient.put()
                .uri("/projects/" + projekt.getProjektId())
                .body(Mono.just(projekt), Projekt.class)
                .retrieve()
                .bodyToMono(Projekt.class);
    }

    public Mono<Void> delete(Integer projektId) {
        return webClient.delete()
                .uri("/projects/" + projektId)
                .retrieve()
                .bodyToMono(Void.class);
    }
}