package ru.job4j.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.handlers.Operation;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.service.MessageService;

import javax.validation.Valid;
import java.util.List;

/**
 * Rest контроллер для работы с моделями Message
 */
@RestController
@RequestMapping("/messages")
public class MessageController {

    /**
     * Бизнес-логика работы с объектом Message
     */
    @Autowired
    private MessageService messageService;

    /**
     * Получить все сообщения
     *
     * @return список сообщений
     */
    @GetMapping
    public ResponseEntity<List<Message>> findAll() {
        return new ResponseEntity<>(
                messageService.findAll(),
                HttpStatus.OK);
    }

    /**
     * Получить сообщение по id
     *
     * @param id идентификатор сообщения
     * @return список сообщений
     */
    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable int id) {
        return messageService.findById(id)
                .map(message -> ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(message))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Message is not found by id %d", id)
                ));
    }

    /**
     * Создать сообщение
     *
     * @param message сообщение
     * @return объект Message
     */
    @PostMapping
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Message> create(@Valid @RequestBody Message message) {
        return new ResponseEntity<>(
                messageService.saveOrUpdate(message),
                HttpStatus.CREATED
        );
    }

    /**
     * Обновить данные сообщения или создать новое
     *
     * @param message объект Message
     */
    @PutMapping
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Message message) {
        messageService.saveOrUpdate(message);
        return ResponseEntity.ok().build();
    }

    /**
     * Обновить данные существующего сообщения
     *
     * @param message объект Message
     */
    @PatchMapping
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Message> patch(@Valid @RequestBody Message message) {
        return new ResponseEntity<>(
                messageService.patch(message),
                HttpStatus.OK
        );
    }

    /**
     * Удалить сообщение по id
     *
     * @param id идентификатор сообщения
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        messageService.delete(id);
        return ResponseEntity.ok().build();
    }
}
