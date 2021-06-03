package ru.job4j.chat.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.domain.Message;
import ru.job4j.chat.service.MessageService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messages;

    public MessageController(MessageService messages) {
        this.messages = messages;
    }

    @PreAuthorize("hasAnyRole('ADMINS','USERS')")
    @GetMapping("/")
    public List<Message> findAll() {
        return new ArrayList<>(this.messages.findAll());
    }

    @PreAuthorize("hasAnyRole('ADMINS','USERS')")
    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable int id) {
        var message = this.messages.findById(id);
        return new ResponseEntity<>(
                message.orElse(new Message()),
                message.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PreAuthorize("hasAnyRole('ADMINS','USERS')")
    @GetMapping("/room/{id}")
    public List<Message> findByRoom(@PathVariable int id) {
        return new ArrayList<>(this.messages.findAllByRoomId(id));
    }

    @PreAuthorize("authentication.principal.id == #message.person.id or hasRole('ADMINS')")
    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
        return new ResponseEntity<>(
                this.messages.save(message),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize("authentication.principal.id == #message.person.id or hasRole('ADMINS')")
    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message message) {
        this.messages.save(message);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMINS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Message message = new Message();
        message.setId(id);
        this.messages.delete(message);
        return ResponseEntity.ok().build();
    }
}
