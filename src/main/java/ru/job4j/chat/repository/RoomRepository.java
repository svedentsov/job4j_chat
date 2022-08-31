package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Room;

/**
 * Интерфейс DAO для комнат
 */
public interface RoomRepository extends CrudRepository<Room, Integer> {
}
