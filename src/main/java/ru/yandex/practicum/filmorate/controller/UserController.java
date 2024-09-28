package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserController(UserService userService, InMemoryUserStorage inMemoryUserStorage) {
        this.userService = userService;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user) {
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody User newUser) {
        return inMemoryUserStorage.updateUser(newUser);
    }

    @DeleteMapping
    public void deleteUser(@RequestBody Long id) {
        inMemoryUserStorage.deleteUser(id);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable("userId") Long userId, @PathVariable("friendId") Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @GetMapping("{userId}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getFriends(@PathVariable("userId") Long userId) {
        return userService.getFriends(userId);
    }

    @DeleteMapping("{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriend(@PathVariable("userId") Long userId, @PathVariable("friendId") Long friendId) {
        userService.delFriend(userId, friendId);
    }

    @GetMapping("{user1Id}/friends/common/{user2Id}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getCommonFriends(@PathVariable("user1Id") long user1Id,
                                             @PathVariable("user2Id") long user2Id) {
        return userService.getCommonFriends(user1Id, user2Id);
    }
}
