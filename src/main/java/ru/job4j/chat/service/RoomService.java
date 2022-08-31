package ru.job4j.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.RoomRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepo;

    public List<Room> findAll() {
        return StreamSupport.stream(
                roomRepo.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    public Optional<Room> findById(int id) {
        return roomRepo.findById(id);
    }

    public Room saveOrUpdate(Room room) {
        return roomRepo.save(room);
    }

    public Room patch(Room room) {
        if (!roomRepo.existsById(room.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        roomRepo.save(room);
        return room;
    }

    public void delete(int id) {
        Room room = new Room();
        room.setId(id);
        roomRepo.delete(room);
    }
}
