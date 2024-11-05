package ru.yandex.practicum.filmorate.dal.rowMappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.FilmGenres;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmGenresRowMapper implements RowMapper<FilmGenres> {
    @Override
    public FilmGenres mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long filmId = rs.getLong("film_id");
        Integer genreId = rs.getInt("id");

        return new FilmGenres(filmId, genreId);
    }
}
