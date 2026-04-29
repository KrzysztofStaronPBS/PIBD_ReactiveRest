package com.project.init;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.project.model.Projekt;
import com.project.model.Student;
import com.project.model.Zadanie;
import com.project.service.ProjectService;
import com.project.service.StudentService;
import com.project.service.ZadanieService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataInitializer {
    private final ProjectService projectService;
    private final StudentService studentService;
    private final ZadanieService zadanieService;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {

        // czyszczenie starych danych
        Mono<Void> cleanup = zadanieService.deleteAll()
                .then(projectService.deleteAll())
                .then(studentService.deleteAll());

        // tworzenie studentów
        Flux<Student> saveStudents = Flux.just(
                Student.builder().imie("Jan").nazwisko("Kowalski").nrIndeksu("123456").email("jankow000@pbs.edu.pl").stacjonarny(true).build(),
                Student.builder().imie("Anna").nazwisko("Nowak").nrIndeksu("234567").email("annnow000@pbs.edu.pl").stacjonarny(true).build(),
                Student.builder().imie("Piotr").nazwisko("Zielinski").nrIndeksu("345678").email("piozie000@pbs.edu.pl").stacjonarny(false).build()
        ).flatMap(studentService::create);

        // tworzenie projektów i zadań dla nich
        Flux<Projekt> saveProjectsWithTasks = Flux.range(1, 10)
                .map(i -> Projekt.builder()
                        .nazwa("Projekt Reaktywny " + i)
                        .opis("Analiza wydajności WebFlux - grupa " + i)
                        .dataOddania(LocalDate.now().plusMonths(i))
                        .build())
                .flatMap(projectService::create)
                .flatMap(p -> {
                    // Dla każdego projektu twórz 3 zadania
                    return Flux.range(1, 3)
                            .map(j -> Zadanie.builder()
                                    .nazwa("Zadanie " + j + " dla projektu " + p.getProjektId())
                                    .opis("Szczegółowy opis zadania " + j)
                                    .kolejnosc(j)
                                    .projektId(p.getProjektId())
                                    .dataczasUtworzenia(LocalDateTime.now())
                                    .build())
                            .flatMap(zadanieService::create)
                            .then(Mono.just(p)); // wróć strumień projektów
                });

        // uruchomienie sekwencji
        cleanup.thenMany(saveStudents)
                .thenMany(saveProjectsWithTasks)
                .subscribe(
                        p -> log.info("Zainicjalizowano projekt: {} (ID: {})", p.getNazwa(), p.getProjektId()),
                        err -> log.error("Błąd inicjalizacji: ", err),
                        () -> log.info("Inicjalizacja danych zakończona sukcesem!")
                );
    }
}