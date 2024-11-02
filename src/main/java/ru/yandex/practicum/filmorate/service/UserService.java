package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.addUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(long userId) {
        return userStorage.getUserById(userId);
    }

    public User updateUser(User user) {
        getUserById(user.getId());
        userStorage.updateUser(user);

        return user;
    }

    public User addFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        if (user.isFriend(friendId)) {
            throw new ConditionsNotMetException("Пользователи уже друзья");
        } else {
            user.addFriend(friendId);
            friend.addFriend(userId);
            userStorage.addFriend(userId, friendId);

            return user;
        }
    }

    public List<User> getFriends(long userId) {
        List<User> userFriends = new ArrayList<>();
        User user = getUserById(userId);

        for (Long id : user.getFriends()) {
            userFriends.add(getUserById(id));
        }
        return userFriends;
    }

    public List<User> getCommonFriends(long user1Id, long user2Id) {
        List<User> commonFriends = new ArrayList<>();
        User user1 = userStorage.getUserById(user1Id);
        User user2 = userStorage.getUserById(user2Id);

        for (long id : user2.getFriends()) {
            if (user1.getFriends().contains(id)) {
                commonFriends.add(userStorage.getUserById(id));
            }
        }
        return commonFriends;
    }

    public User delFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);

        if (!user.isFriend(friendId)) {
            throw new ConditionsNotMetException("Пользователи не друзья");
        } else {
            user.removeFriend(friendId);
            userStorage.delFriend(userId, friendId);

            return user;
        }
    }
}
