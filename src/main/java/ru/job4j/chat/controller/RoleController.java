package ru.job4j.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.handlers.Operation;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.service.RoleService;

import javax.validation.Valid;
import java.util.List;

/**
 * Rest контроллер для работы с объектами Role
 */
@RestController
@RequestMapping("/roles")
public class RoleController {

    /**
     * Бизнес-логика работы с объектом Роли
     */
    @Autowired
    private RoleService roleService;

    /**
     * Получить все роли
     *
     * @return список ролей
     */
    @GetMapping
    public List<Role> findAll() {
        return roleService.findAll();
    }

    /**
     * Получить роль по её идентификатору
     *
     * @param id идентификатор роли
     * @return ролевой объект
     */
    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable int id) {
        return roleService.findById(id)
                .map(role -> new ResponseEntity<>(role, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Role is not found by id %d", id)
                ));
    }

    /**
     * Создать роль
     *
     * @param role ролевой объект
     * @return создал объект роли
     */
    @PostMapping
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Role> create(@Valid @RequestBody Role role) {
        return new ResponseEntity<>(
                roleService.saveOrUpdate(role),
                HttpStatus.CREATED
        );
    }

    /**
     * Обновить данные роли или создать новую
     *
     * @param role ролевой объект
     */
    @PutMapping
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Role role) {
        roleService.saveOrUpdate(role);
        return ResponseEntity.ok().build();
    }

    /**
     * Обновить данные существующей роли
     *
     * @param role ролевой объект
     */
    @PatchMapping
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Role> patch(@Valid @RequestBody Role role) {
        return new ResponseEntity<>(
                roleService.patch(role),
                HttpStatus.OK
        );
    }

    /**
     * Удалить роль по её идентификатору
     *
     * @param id идентификатор роли
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @PathVariable int id) {
        roleService.delete(id);
        return ResponseEntity.ok().build();
    }
}
