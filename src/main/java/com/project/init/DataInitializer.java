package com.project.init;

import java.time.LocalDate;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.project.model.Projekt;
import com.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataInitializer {
    private final ProjectService projectService;
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        var saveProjects =
                Flux.just("Projekt 1", "Projekt 2", "Projekt 3", "Projekt 4", "Projekt 5")
                        .map(nazwa -> Projekt.builder()
                                .nazwa(nazwa)
                                .opis(String.format("Opis testowy projektu - %s", nazwa))
                                .dataOddania(LocalDate.of(2026, 7, 1))
                                .build())
                        .flatMap(projectService::create);
        projectService.deleteAll()
                .thenMany(saveProjects)
                .thenMany(projectService.findAll())
                .subscribe(p -> log.info("student: {} {}", p.getProjektId(), p.getNazwa()));
    }
}