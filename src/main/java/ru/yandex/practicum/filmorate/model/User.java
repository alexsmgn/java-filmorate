package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.*;

@Data
@EqualsAndHashCode(of = {"id"})
@Slf4j
public class User {
    private Long id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Pattern(regexp = "^\\S+$")
    private String login;
    private String name;
    @Past
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

    public boolean isFriend(Long id) {
        if (friends == null) {
            return false;
        } else {
            return friends.contains(id);
        }
    }
}
