package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Person;

/**
 * Интерфейс DAO для пользователей
 */
public interface PersonRepository extends CrudRepository<Person, Integer> {
    Person findByUsername(String username);
}
