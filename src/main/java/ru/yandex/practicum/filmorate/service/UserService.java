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
        if (userId == friendId) {
            log.error("id {} пользователя совпадает с id {} друга", userId, friendId);
            throw new ValidationException("id пользователя не может совпадать с id друга");
        }

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.info("Пользователи с id {} и {} теперь друзья", userId, friendId);
    }

    public Collection<User> getFriends(long userId) {
        List<User> userFriends = new ArrayList<>();
        User user = getUserById(userId);
        for (Long id : user.getFriends()) {
            userFriends.add(inMemoryUserStorage.users.get(id));
        }
        return userFriends;
    }

    public Collection<User> getCommonFriends(long user1Id, long user2Id) {
        List<User> commonFriends = new ArrayList<>();
        User user1 = getUserById(user1Id);
        User user2 = getUserById(user2Id);
        for (long id : user1.getFriends()) {
            if (user2.getFriends().contains(id)) {
                commonFriends.add(getUserById(id));
            }
        }
        return commonFriends;
    }

    public void delFriend(long userId, long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info("Пользователи {} и {} больше не друзья", userId, friendId);
    }

    public User addUser(User user) {
        return inMemoryUserStorage.addUser(user);
    }

    public User updateUser(User newUser) {
        return inMemoryUserStorage.updateUser(newUser);
    }

    public void deleteUser(long id) {
        inMemoryUserStorage.deleteUser(id);
    }
}
