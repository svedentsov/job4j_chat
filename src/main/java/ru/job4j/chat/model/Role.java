package ru.job4j.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.job4j.chat.handlers.Operation;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/**
 * Модель роли
 */
@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "Id must be more than 0", groups = Operation.OnUpdate.class)
    private int id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Name must be not empty", groups = {
            Operation.OnCreate.class, Operation.OnUpdate.class
    })
    private String name;

    @OneToMany(mappedBy = "role")
    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    private Set<Person> persons = new HashSet<>();
}
