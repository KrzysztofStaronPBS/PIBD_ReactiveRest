package com.project.service;

import com.project.model.Student;
import com.project.repository.StudentRepository;
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
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentRepository;
    private final TransactionalOperator transactionalOperator;
    private final R2dbcEntityTemplate entityTemplate;

    @Override
    public Flux<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Mono<Student> findById(Integer studentId) {
        return studentRepository.findById(studentId);
    }

    @Override
    public Flux<Student> findByNazwa(String nazwa) {
        return studentRepository.findByNazwiskoContains(nazwa);
    }

    @Override
    public Mono<Student> create(Student student) {
        return entityTemplate.insert(Student.class).using(student);
    }

    @Override
    public Mono<Student> update(Student student) {
        return studentRepository
                .findById(student.getStudentId())
                .flatMap(s -> {
                    s.setImie(student.getImie());
                    s.setNazwisko(student.getNazwisko());
                    s.setEmail(student.getEmail());
                    s.setNrIndeksu(student.getNrIndeksu());
                    s.setStacjonarny(student.getStacjonarny());
                    return studentRepository.save(s);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student nie został znaleziony")))
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Void> delete(Integer studentId) {
        return studentRepository.deleteById(studentId);
    }

    @Override
    public Mono<Void> deleteAll() {
        return studentRepository.deleteAll();
    }
}
