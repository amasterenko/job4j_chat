package ru.job4j.chat.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.service.PersonService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonService persons;

    public PersonController(PersonService persons) {
        this.persons = persons;
    }

    @GetMapping("/")
    public List<Person> findAll() {
        return new ArrayList<>(this.persons.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = this.persons.findById(id);
        return new ResponseEntity<>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        return new ResponseEntity<>(
                this.persons.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        this.persons.save(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        this.persons.delete(person);
        return ResponseEntity.ok().build();
    }
}
