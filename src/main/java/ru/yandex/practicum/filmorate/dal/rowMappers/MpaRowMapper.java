package ru.yandex.practicum.filmorate.dal.rowMappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MpaRowMapper implements RowMapper<Mpa> {
    @Override
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer mpaId = rs.getInt("id");
        String name = rs.getString("name");

        return new Mpa(mpaId, name);
    }
}
