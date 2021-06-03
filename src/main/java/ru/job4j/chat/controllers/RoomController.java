package ru.job4j.chat.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.domain.Room;
import ru.job4j.chat.service.RoomService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService rooms;

    public RoomController(RoomService rooms) {
        this.rooms = rooms;
    }

    @PreAuthorize("hasAnyRole('ADMINS', 'USERS')")
    @GetMapping("/")
    public List<Room> findAll() {
        return new ArrayList<>(this.rooms.findAll());
    }

    @PreAuthorize("hasRole('ADMINS')")
    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable int id) {
        var room = this.rooms.findById(id);
        return new ResponseEntity<>(
                room.orElse(new Room()),
                room.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PreAuthorize("hasRole('ADMINS')")
    @PostMapping("/")
    public ResponseEntity<Room> create(@RequestBody Room room) {
        return new ResponseEntity<>(
                this.rooms.save(room),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasRole('ADMINS')")
    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Room room) {
        this.rooms.save(room);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMINS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Room room = new Room();
        room.setId(id);
        this.rooms.delete(room);
        return ResponseEntity.ok().build();
    }
}
