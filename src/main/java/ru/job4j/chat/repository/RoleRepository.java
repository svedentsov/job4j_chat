package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Role;

/**
 * Интерфейс DAO для ролей
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
