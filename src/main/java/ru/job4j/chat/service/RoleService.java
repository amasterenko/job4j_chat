package ru.job4j.chat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.chat.domain.Role;
import ru.job4j.chat.exceptions.BadRequestException;
import ru.job4j.chat.repositories.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private static final Logger LOG = LoggerFactory.getLogger(RoleService.class);
    private final RoleRepository roles;

    public RoleService(RoleRepository roles) {
        this.roles = roles;
    }

    public List<Role> findAll() {
        List<Role> rsl = new ArrayList<>();
        roles.findAll().forEach(rsl::add);
        return rsl;
    }

    public Optional<Role> findById(int id) {
        return roles.findById(id);
    }

    public Role save(Role role) {
        try {
            return roles.save(role);
        } catch (Exception e) {
            LOG.error("exception:", e);
            throw new BadRequestException();
        }
    }

    public void delete(Role role) {
        roles.delete(role);
    }

    public Optional<Role> findByName(String name) {
        return roles.findByName(name);
    }
}
