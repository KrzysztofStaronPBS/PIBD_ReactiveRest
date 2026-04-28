package com.project.repository;

import com.project.model.Zadanie;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ZadanieRepository extends ReactiveCrudRepository<Zadanie, Integer> {
    @Query("SELECT * FROM zadanie where nazwa ILIKE '%' || :nazwa || '%'")
    Flux<Zadanie> findByNazwaContains(String nazwa);
}
