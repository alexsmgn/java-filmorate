package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.*;

@Data
@EqualsAndHashCode(of = {"id"})
@Slf4j
public class User {
    private Long id;
    @Email(message = "некорректный email")
    @NotBlank
    private String email;
    @NotBlank(message = "некорректный login")
    private String login;
    private String name;
    @Past(message = "дата рождения не может быть из будущего")
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("email", email);
        values.put("login", login);
        values.put("username", name);
        values.put("birthday", birthday);

        return values;
    }

    public User updateUser(User user) {
        this.setEmail(user.getEmail());
        this.setLogin(user.getLogin());
        this.setName(user.getName());
        this.setBirthday(user.getBirthday());

        return this;
    }

    public void addFriend(Long id) {
        friends.add(id);
    }

    public void addFriends(List<Long> friends) {
        for (Long id : friends) {
            this.friends.add(id);
        }
    }

    public void removeFriend(Long id) {
        friends.remove(id);
    }

    public boolean isFriends(Long id) {
        if (friends == null) {
            return false;
        } else {
            return friends.contains(id);
        }
    }
}
