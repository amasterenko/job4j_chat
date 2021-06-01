package ru.job4j.chat.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.domain.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {

}
