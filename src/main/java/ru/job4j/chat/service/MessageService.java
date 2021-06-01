package ru.job4j.chat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.chat.domain.Message;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.domain.Room;
import ru.job4j.chat.exceptions.BadRequestException;
import ru.job4j.chat.exceptions.NotFoundException;
import ru.job4j.chat.repositories.MessageRepository;
import ru.job4j.chat.repositories.PersonRepository;
import ru.job4j.chat.repositories.RoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);
    private final MessageRepository messages;
    private final PersonRepository persons;
    private final RoomRepository rooms;

    public MessageService(MessageRepository messages,
                          PersonRepository persons,
                          RoomRepository rooms) {
        this.messages = messages;
        this.persons = persons;
        this.rooms = rooms;
    }

    public List<Message> findAll() {
        List<Message> rsl = new ArrayList<>();
        messages.findAll().forEach(rsl::add);
        return rsl;
    }

    public List<Message> findAllByRoomId(int id) {
        return new ArrayList<>(messages.findAllByRoomId(id));
    }

    public Optional<Message> findById(int id) {
        Message message = messages.findById(id).orElseThrow(NotFoundException::new);
        return Optional.of(message);
    }

    public Message save(Message message) {
        try {
            Room room = rooms.findById(message.getRoom().getId())
                    .orElseThrow(NotFoundException::new);
            message.setRoom(room);
            Person person = persons.findById(message.getPerson().getId())
                    .orElseThrow(NotFoundException::new);
            message.setPerson(person);
            return messages.save(message);
        } catch (Exception e) {
            LOG.error("exception:", e);
            throw new BadRequestException();
        }
    }

    public void delete(Message message) {
        messages.delete(message);
    }
}
