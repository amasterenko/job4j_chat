package ru.job4j.chat.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.domain.Role;
import ru.job4j.chat.service.RoleService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/roles")
@PreAuthorize("hasRole('ADMINS')")
public class RoleController {

    private final RoleService roles;

    public RoleController(RoleService roles) {
        this.roles = roles;
    }

    @GetMapping("/")
    public List<Role> findAll() {
        return new ArrayList<>(this.roles.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable int id) {
        var role = this.roles.findById(id);
        return new ResponseEntity<>(
                role.orElse(new Role()),
                role.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Role> create(@RequestBody Role role) {
        return new ResponseEntity<>(
                this.roles.save(role),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Role role) {
        this.roles.save(role);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Role role = new Role();
        role.setId(id);
        this.roles.delete(role);
        return ResponseEntity.ok().build();
    }
}
