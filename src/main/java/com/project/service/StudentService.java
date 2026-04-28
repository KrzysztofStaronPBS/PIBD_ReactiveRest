package com.project.service;

import com.project.model.Student;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface StudentService {
    Flux<Student> findAll();
    Mono<Student> findById(Integer studentId);
    Flux<Student> findByNazwa(String nazwa);
    Mono<Student> create(Student student);
    Mono<Student> update(Student student);
    Mono<Void> delete(Integer studentId);
    Mono<Void> deleteAll();
}
