package ru.job4j.chat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.domain.Role;
import ru.job4j.chat.exceptions.BadRequestException;
import ru.job4j.chat.exceptions.NotFoundException;
import ru.job4j.chat.repositories.PersonRepository;
import ru.job4j.chat.repositories.RoleRepository;
import ru.job4j.chat.security.CustomUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService implements UserDetailsService {
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
        } catch (NotFoundException e) {
            throw new NotFoundException();
        } catch (Exception e) {
            LOG.error("exception:", e);
            throw new BadRequestException();
        }
    }

    public void delete(Person person) {
        persons.delete(person);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person user = persons.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUser(
                user.getId(),
                user.getName(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().getName()))
        );
    }
}
