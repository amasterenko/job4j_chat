package ru.job4j.chat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.domain.Role;
import ru.job4j.chat.exceptions.BadRequestException;
import ru.job4j.chat.exceptions.NotFoundException;
import ru.job4j.chat.repositories.PersonRepository;
import ru.job4j.chat.repositories.RoleRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private static final Logger LOG = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository persons;
    private final RoleRepository roles;

    public PersonService(PersonRepository persons, RoleRepository roles) {
        this.persons = persons;
        this.roles = roles;
    }

    public List<Person> findAll() {
        List<Person> rsl = new ArrayList<>();
        persons.findAll().forEach(rsl::add);
        return rsl;
    }

    public Optional<Person> findById(int id) {
        Person person = persons.findById(id).orElseThrow(NotFoundException::new);
        return Optional.of(person);
    }

    public Person save(Person person) {
        try {
            if (person.getRole() == null) {
                throw new BadRequestException();
            }
            Role role = roles.findById(person.getRole().getId())
                    .orElseThrow(NotFoundException::new);
            person.setRole(role);
            return persons.save(person);
        } catch (Exception e) {
            LOG.error("exception:", e);
            throw new BadRequestException();
        }
    }

    @Transactional
    public void delete(Person person) {
        persons.delete(person);
    }
}
