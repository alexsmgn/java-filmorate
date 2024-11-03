package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.rowMappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository
public class GenreRepository implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(Integer id) {
        String query = "SELECT * FROM genre WHERE id = ?";

        return jdbcTemplate.query(query, new GenreRowMapper(), id).stream().findAny()
                .orElseThrow(() -> new NotFoundException("Жанр с id " + id + " не найден"));
    }

    @Override
    public Set<Genre> getGenres() {
        String query = "SELECT * FROM genre";

        return new HashSet<>(jdbcTemplate.query(query, new GenreRowMapper()));
    }

    @Override
    public Set<Genre> getFilmGenres(Long id) {
        String query = "with cte as (select id from film_genres where film_id = ?) " +
                "select * from genre join cte on genre.id = cte.id ";

        return new HashSet<>(jdbcTemplate.query(query, new GenreRowMapper(), id));
    }

    @Override
    public void setFilmGenres(Film film) {
        String query = "INSERT INTO film_genres (film_id, id)" +
                " values (?, ?)";

        List<Genre> genres = new ArrayList<>(film.getGenres());

        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, film.getId());
                ps.setLong(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }

    @Override
    public void clearFilmGenre(Long id) {
        String query = "DELETE FROM film_genres WHERE film_id = ?";

        jdbcTemplate.update(query, id);
    }
}
