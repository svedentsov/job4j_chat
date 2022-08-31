package ru.job4j.chat.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import ru.job4j.chat.handlers.Operation;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

/**
 * Модель сообщения
 */
@Data
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "Id must be more than 0", groups = Operation.OnUpdate.class)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Calendar created;

    @Column(nullable = false)
    @NotBlank(message = "Text must be not empty", groups = {
            Operation.OnCreate.class, Operation.OnUpdate.class
    })
    private String text;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @NotNull(message = "Room must be not empty", groups = {
            Operation.OnCreate.class, Operation.OnUpdate.class
    })
    private Room room;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    @NotNull(message = "Person must be not empty", groups = {
            Operation.OnCreate.class, Operation.OnUpdate.class
    })
    private Person person;
}
