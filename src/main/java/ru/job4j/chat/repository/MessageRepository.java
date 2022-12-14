package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Message;

/**
 * Интерфейс DAO для сообщений
 */
public interface MessageRepository extends CrudRepository<Message, Integer> {
}
