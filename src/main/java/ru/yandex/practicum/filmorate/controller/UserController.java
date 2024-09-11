package ru.yandex.practicum.filmorate.controller;

import exceptions.NotFoundException;
import exceptions.ValidationException;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        user.setId(getNextId());
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Поле Email не заполнено или пропущен символ @ при добавлении");
            throw new ValidationException("Email пользователя не может быть пустым и должен содержать символ @");
        }
        if (user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            log.error("Поле Login не заполнено или содержит пробелы при добавлении");
            throw new ValidationException("Login пользователя не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.info("Вместо имени пользователя подставлен Login");
        } else {
            user.setName(user.getName());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Введена неверная дата рождения при добавлении");
            throw new ValidationException("Пользователь из будущего, что не возможно!");
        }
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.error("Не введен id пользователя при изменении");
            throw new ValidationException("id пользователя должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getEmail() == null || newUser.getEmail().isBlank() || !newUser.getEmail().contains("@")) {
                log.error("Поле Email не заполнено или пропущен символ @ при изменении");
                throw new ValidationException("Email пользователя не может быть пустым и должен содержать символ @");
            }
            if (newUser.getLogin().contains(" ") || newUser.getLogin().isBlank()) {
                log.error("Поле Login не заполнено или содержит пробелы при изменении");
                throw new ValidationException("Login пользователя не может быть пустым и содержать пробелы");
            }
            if (newUser.getBirthday().isAfter(LocalDate.now())) {
                log.error("Введена неверная дата рождения при изменении");
                throw new ValidationException("Пользователь из будущего, что не возможно!");
            }
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            if (newUser.getName().isBlank()) {
                newUser.setName(newUser.getLogin());
            }
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            return oldUser;
        }
        log.error("Пользователь с id {} не найден", newUser.getId());
        throw new NotFoundException("Пользователь с id =" + newUser.getId() + "не найден");
    }
}
