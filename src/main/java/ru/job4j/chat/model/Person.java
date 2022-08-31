package ru.job4j.chat.model;

import lombok.Data;
import ru.job4j.chat.handlers.Operation;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Модель пользователя
 */
@Data
@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null", groups = Operation.OnUpdate.class)
    private int id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username must be not empty", groups = {
            Operation.OnCreate.class, Operation.OnUpdate.class
    })
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Password must be not empty", groups = {
            Operation.OnCreate.class, Operation.OnUpdate.class
    })
    private String password;

    @ManyToOne
    @JoinTable(
            name = "role_of_persons",
            joinColumns = {@JoinColumn(name = "person_id", nullable = false, unique = true)},
            inverseJoinColumns = {@JoinColumn(name = "role_id", nullable = false)}
    )
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "persons_in_rooms",
            joinColumns = {@JoinColumn(name = "person_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "room_id", nullable = false)}
    )
    private Set<Room> rooms = new HashSet<>();

    public void addRoom(Room room) {
        this.rooms.add(room);
    }
}
