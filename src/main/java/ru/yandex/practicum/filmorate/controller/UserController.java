package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @GetMapping("{userId}/friends")
    public Collection<User> getFriends(@PathVariable Long userId) {
        return userService.getFriends(userId);
    }

    @DeleteMapping("{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public User deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.delFriend(userId, friendId);
    }

    @GetMapping("{user1Id}/friends/common/{user2Id}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getCommonFriends(@PathVariable long user1Id,
                                             @PathVariable long user2Id) {
        return userService.getCommonFriends(user1Id, user2Id);
    }
}
