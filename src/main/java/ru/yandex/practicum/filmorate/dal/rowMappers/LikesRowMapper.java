package ru.yandex.practicum.filmorate.dal.rowMappers;


import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Likes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LikesRowMapper implements RowMapper<Likes> {
    @Override
    public Likes mapRow(ResultSet rs, int roNum) throws SQLException {
        Long userId = rs.getLong("user_id");
        Long filmId = rs.getLong("film_id");

        return new Likes(userId, filmId);
    }
}
