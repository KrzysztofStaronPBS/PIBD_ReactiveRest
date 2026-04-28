package com.project.repository;

import com.project.model.Student;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StudentRepository extends ReactiveCrudRepository<Student, Integer> {
    @Query("SELECT * FROM student WHERE nazwisko ILIKE '%' || :nazwisko || '%'")
    Flux<Student> findByNazwiskoContains(String nazwisko);
}
