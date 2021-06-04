package ru.job4j.chat.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.domain.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {
    Person findByName(String name);
}
