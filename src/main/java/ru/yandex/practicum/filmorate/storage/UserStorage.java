package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User newUser);

    void deleteUser(Long id);

    void userValidator(User user);
}
