package ru.job4j.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.handlers.Operation;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.RoomService;

import javax.validation.Valid;
import java.util.List;

/**
 * Rest контроллер для работы с моделями Room
 */
@RestController
@RequestMapping("/rooms")
public class RoomController {

    /**
     * Бизнес-логин для работы с объектом Room
     */
    @Autowired
    private RoomService roomService;

    /**
     * Получить все комнаты
     */
    @GetMapping
    public List<Room> findAll() {
        return roomService.findAll();
    }

    /**
     * Получить комнату по id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable int id) {
        return roomService.findById(id)
                .map(room -> new ResponseEntity<>(room, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Room is not found by id %d", id)
                ));
    }

    /**
     * Создать комнату
     */
    @PostMapping
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Room> create(@Valid @RequestBody Room room) {
        return new ResponseEntity<>(
                roomService.saveOrUpdate(room),
                HttpStatus.CREATED
        );
    }

    /**
     * Обновить данные комнаты или создать новую
     */
    @PutMapping
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Room room) {
        roomService.saveOrUpdate(room);
        return ResponseEntity.ok().build();
    }

    /**
     * Обновить данные существующей комнаты
     */
    @PatchMapping
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Room> patch(@Valid @RequestBody Room room) {
        return new ResponseEntity<>(
                roomService.patch(room),
                HttpStatus.OK
        );
    }

    /**
     * Удалить комнату по id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @PathVariable int id) {
        roomService.delete(id);
        return ResponseEntity.ok().build();
    }
}
