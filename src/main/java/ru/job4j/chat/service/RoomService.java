package ru.job4j.chat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.chat.domain.Room;
import ru.job4j.chat.exceptions.BadRequestException;
import ru.job4j.chat.repositories.RoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private static final Logger LOG = LoggerFactory.getLogger(RoomService.class);
    private final RoomRepository rooms;

    public RoomService(RoomRepository rooms) {
        this.rooms = rooms;
    }

    public List<Room> findAll() {
        List<Room> rsl = new ArrayList<>();
        rooms.findAll().forEach(rsl::add);
        return rsl;
    }

    public Optional<Room> findById(int id) {
        return rooms.findById(id);
    }

    public Room save(Room room) {
        try {
            return rooms.save(room);
        } catch (Exception e) {
            LOG.error("exception:", e);
            throw new BadRequestException();
        }
    }

    public void delete(Room room) {
        rooms.delete(room);
    }
}
