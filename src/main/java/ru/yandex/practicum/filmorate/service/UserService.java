package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Collection<User> findAll() {
        return inMemoryUserStorage.users.values();
    }

    public User getUserById(long userId) {
        if (inMemoryUserStorage.users.containsKey(userId)) {
            return inMemoryUserStorage.users.get(userId);
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public void addFriend(long userId, long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        if (!inMemoryUserStorage.users.containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        log.error("Пользователь с id {} не найден", userId);
        if (!inMemoryUserStorage.users.containsKey(friendId)) {
            throw new NotFoundException("Пользователь кого хотят добавить в друзья не найден");
        }
        log.error("Пользователь друг с id {} не найден", friendId);
        if (userId == friendId) {
            throw new ValidationException("id пользователя не может совпадать с id друга");
        }
        log.error("id {} пользователя совпадает с id {} друга", userId, friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.info("Пользователи с id {} и {} теперь друзья", userId, friendId);
    }


    public Collection<User> getFriends(long userId) {
        List<User> userFriends = new ArrayList<>();
        User user = getUserById(userId);
        if (inMemoryUserStorage.users.containsKey(userId)) {
            for (Long id : user.getFriends()) {
                userFriends.add(inMemoryUserStorage.users.get(id));
            }
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
        return userFriends;
    }

    public void delFriend(long userId, long friendId) {
        if (!inMemoryUserStorage.users.containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (!inMemoryUserStorage.users.containsKey(friendId)) {
            throw new NotFoundException("Друг не найден");
        }
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info("Пользователи {} и {} больше не друзья", userId, friendId);
    }
}
