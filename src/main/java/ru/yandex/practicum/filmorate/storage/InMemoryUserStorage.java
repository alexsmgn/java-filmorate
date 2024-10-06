package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    public final Map<Long, User> users = new HashMap<>();

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public User addUser(User user) {
        user.setId(getNextId());
        userValidator(user);
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.info("Вместо имени пользователя подставлен Login");
        } else {
            user.setName(user.getName());
        }
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        if (newUser.getId() == null) {
            log.error("Не введен id пользователя при изменении");
            throw new ValidationException("id пользователя должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            userValidator(newUser);
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

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
        log.info("Пользователь с id {} удален", id);
    }

    @Override
    public void userValidator(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Поле Email не заполнено или пропущен символ @ при добавлении");
            throw new ValidationException("Email пользователя не может быть пустым и должен содержать символ @");
        }
        if (user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            log.error("Поле Login не заполнено или содержит пробелы при добавлении");
            throw new ValidationException("Login пользователя не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Введена неверная дата рождения при добавлении");
            throw new ValidationException("Пользователь из будущего, что не возможно!");
        }
    }
}
