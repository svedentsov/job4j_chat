package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.handlers.Operation;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * REST-контроллер для работы с объектами Person
 */
@RestController
@RequestMapping("/users")
public class PersonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class.getSimpleName());

    /**
     * Бизнес-логика работы с объектом Person
     */
    @Autowired
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Получить всех пользователей
     */
    @GetMapping
    public List<Person> findAll() {
        return personService.findAll();
    }

    /**
     * Получить пользователя по его id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        return personService.findById(id)
                .map(person -> new ResponseEntity<>(person, HttpStatus.OK))
                .orElseThrow(() -> generateExceptionPersonNotFoundById(id));
    }

    /**
     * Получить роль пользователя по id пользователя
     */
    @GetMapping("/{id}/role")
    public ResponseEntity<Role> findPersonRoleById(@PathVariable int id) {
        return personService.findPersonRoleById(id)
                .map(role -> new ResponseEntity<>(role, HttpStatus.OK))
                .orElseThrow(() -> generateExceptionPersonNotFoundById(id));
    }

    /**
     * Получить список комнат, в которых состоит пользователь по id пользователя
     */
    @GetMapping("/{id}/rooms")
    public ResponseEntity<Set<Room>> findPersonRoomsById(@PathVariable int id) {
        return personService.findPersonRoomsById(id)
                .map(rooms -> new ResponseEntity<>(rooms, HttpStatus.OK))
                .orElseThrow(() -> generateExceptionPersonNotFoundById(id));
    }

    /**
     * Создать пользователя
     */
    @PostMapping("/sign-up")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Person> signUp(@Valid @RequestBody Person person) {
        return new ResponseEntity<>(
                personService.saveOrUpdate(person), HttpStatus.CREATED);
    }

    /**
     * Обновить данные пользователя или создать нового
     */
    @PutMapping
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Person> update(@Valid @RequestBody Person person) {
        return new ResponseEntity<>(
                personService.saveOrUpdate(person), HttpStatus.OK);
    }

    /**
     * Обновить данные существующего пользователя
     */
    @PatchMapping
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Person> patch(@Valid @RequestBody Person person) {
        return new ResponseEntity<>(
                personService.patch(person),
                HttpStatus.OK
        );
    }

    /**
     * Обновить роль пользователя по id пользователя
     */
    @PutMapping("/{id}/role")
    public ResponseEntity<Void> updateRole(@PathVariable int id, @RequestBody Role role) {
        try {
            personService.updatePersonRole(id, role);
        } catch (IllegalArgumentException e) {
            throw generateExceptionPersonNotFoundById(id);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Добавить комнату пользователю (зайти в комнату) по id пользователя
     */
    @PutMapping("/{id}/rooms")
    public ResponseEntity<Void> addRoom(@PathVariable int id, @RequestBody Room room) {
        try {
            personService.addPersonRoom(id, room);
        } catch (IllegalArgumentException e) {
            throw generateExceptionPersonNotFoundById(id);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Удалить пользователя по его id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        personService.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Удалить роль пользователя по id пользователя
     */
    @DeleteMapping("/{id}/role")
    public ResponseEntity<Void> deleteRole(@PathVariable int id) {
        try {
            personService.deletePersonRole(id);
        } catch (IllegalArgumentException e) {
            throw generateExceptionPersonNotFoundById(id);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Удалить комнату пользователя (выйти из комнаты) по id пользователя и id комнаты
     */
    @DeleteMapping("/{id}/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable int id, @PathVariable int roomId) {
        try {
            personService.deletePersonRoom(id, roomId);
        } catch (IllegalArgumentException e) {
            throw generateExceptionPersonNotFoundById(id);
        }
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", e.getMessage());
                put("type", e.getClass());
            }
        }));
        LOGGER.error(e.getLocalizedMessage());
    }

    private ResponseStatusException generateExceptionPersonNotFoundById(int id) {
        return new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Person is not found by id %d", id)
        );
    }
}
