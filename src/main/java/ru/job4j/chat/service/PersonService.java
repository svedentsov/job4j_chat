package ru.job4j.chat.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepo;
    @Autowired
    private BCryptPasswordEncoder encoder;

    public List<Person> findAll() {
        return StreamSupport.stream(
                personRepo.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    public Optional<Person> findById(int personId) {
        return personRepo.findById(personId);
    }

    public Optional<Role> findPersonRoleById(int personId) {
        return personRepo.findById(personId)
                .map(person -> person.getRole() == null ? new Role() : person.getRole());
    }

    public Optional<Set<Room>> findPersonRoomsById(int personId) {
        return personRepo.findById(personId)
                .map(Person::getRooms);
    }

    public Person saveOrUpdate(Person person) {
        validatePassword(person.getPassword());
        person.setPassword(encoder.encode(person.getPassword()));
        return personRepo.save(person);
    }

    public Person patch(Person person) {
        if (!personRepo.existsById(person.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        personRepo.save(person);
        return person;
    }

    public void updatePersonRole(int personId, Role role) {
        Person person = personRepo.findById(personId)
                .orElseThrow(IllegalArgumentException::new);
        if (role.getId() == 0) {
            throw new NullPointerException("Role id mustn't be empty");
        }
        person.setRole(role);
        personRepo.save(person);
    }

    public void addPersonRoom(int personId, Room room) {
        Person person = personRepo.findById(personId)
                .orElseThrow(IllegalArgumentException::new);
        if (room.getId() == 0) {
            throw new NullPointerException("Room id mustn't be empty");
        }
        person.addRoom(room);
        personRepo.save(person);
    }

    public void delete(int personId) {
        Person person = new Person();
        person.setId(personId);
        personRepo.delete(person);
    }

    public void deletePersonRole(int personId) {
        Person person = personRepo.findById(personId)
                .orElseThrow(IllegalArgumentException::new);
        person.setRole(null);
        personRepo.save(person);
    }

    public void deletePersonRoom(int personId, int roomId) {
        Person person = personRepo.findById(personId)
                .orElseThrow(IllegalArgumentException::new);
        person.getRooms().removeIf(room -> room.getId() == roomId);
    }

    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Invalid password. Password must be at least 8 characters long.");
        }
        boolean check = false;
        boolean upperCase = false;
        boolean lowerCase = false;
        boolean number = false;
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                upperCase = true;
            }
            if (Character.isLowerCase(ch)) {
                lowerCase = true;
            }
            if (Character.isDigit(ch)) {
                number = true;
            }
            check = upperCase && lowerCase && number;
            if (check) {
                break;
            }
        }
        if (!check) {
            throw new IllegalArgumentException(
                    "Invalid password. The password must contain at least three character categories"
                            + " among the following: uppercase characters, lowercase characters and digits"
            );
        }
    }
}
