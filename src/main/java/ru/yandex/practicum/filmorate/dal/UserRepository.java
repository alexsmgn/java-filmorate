package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.rowMappers.UserRowMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Repository
public class UserRepository implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        Long userId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();
        user.setId(userId);

        return user;
    }

    @Override
    public void updateUser(User user) {
        String query = "UPDATE users SET email = ?, login = ?, username = ?, birthday = ? WHERE user_id = ?";

        jdbcTemplate.update(query, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());

    }

    @Override
    public List<User> getUsers() {
        String query = "SELECT u.*, f.friend_id FROM users AS u LEFT JOIN friends AS f ON u.user_id = f.user_id";

        List<User> users = jdbcTemplate.query(query, new UserRowMapper());

        for (User user : users) {
            this.getFriends(user);
        }

        return users;
    }

    @Override
    public User getUserById(Long id) {
        String query = "SELECT u.* FROM users AS u LEFT JOIN friends AS f ON u.user_id = f.user_id WHERE u.user_id = ?";

        User user = jdbcTemplate.query(query, new UserRowMapper(), id).stream().findAny()
                .orElseThrow(() -> new NotFoundException("Пользователь с id" + id + "не найден"));

        this.getFriends(user);

        return user;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String query = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";

        jdbcTemplate.update(query, userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        String query = "DELETE FROM friends where user_id = ? AND friend_id = ?";

        jdbcTemplate.update(query, userId, friendId);
    }

    private void getFriends(User user) {
        String query = "SELECT friend_id FROM friends WHERE user_id = ?";

        List<Long> friends = jdbcTemplate.query(query, (rs, rowNum) -> rs.getLong("friend_id"), user.getId());
        user.addFriends(friends);
    }
}
