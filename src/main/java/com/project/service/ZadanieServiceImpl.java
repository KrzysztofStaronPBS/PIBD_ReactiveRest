package com.project.service;

import com.project.model.Zadanie;
import com.project.repository.ZadanieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ZadanieServiceImpl implements ZadanieService{
    private final ZadanieRepository zadanieRepository;
    private final TransactionalOperator transactionalOperator;
    private final R2dbcEntityTemplate entityTemplate;

    @Override
    public Flux<Zadanie> findAll() {
        return zadanieRepository.findAll();
    }

    @Override
    public Mono<Zadanie> findById(Integer zadanieId) {
        return zadanieRepository.findById(zadanieId);
    }

    @Override
    public Flux<Zadanie> findByNazwa(String nazwa) {
        return zadanieRepository.findByNazwaContains(nazwa);
    }

    @Override
    public Mono<Zadanie> create(Zadanie zadanie) {
        return entityTemplate.insert(Zadanie.class).using(zadanie);
    }

    @Override
    public Mono<Zadanie> update(Zadanie zadanie) {
        return zadanieRepository
                .findById(zadanie.getZadanieId())
                .flatMap(z ->{
                    z.setNazwa(zadanie.getNazwa());
                    z.setOpis(zadanie.getOpis());
                    z.setKolejnosc(zadanie.getKolejnosc());
                    return zadanieRepository.save(z);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Zadanie nie zostało znalezione")))
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Void> delete(Integer zadanieId) {
        return zadanieRepository.deleteById(zadanieId);
    }

    @Override
    public Mono<Void> deleteAll() {
        return zadanieRepository.deleteAll();
    }
}
