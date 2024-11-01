package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.rowMappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.rowMappers.LikesRowMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Likes;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository
public class FilmRepository implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        Long filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue();

        film.setId(filmId);

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String query = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rate = ?" +
                "mpa = ? WHERE film_id = ?";

        jdbcTemplate.update(query, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getRate(), film.getMpa().getId(), film.getId());

        return film;
    }

    @Override
    public List<Film> getFilms() {
        String query = "SELECT f.*, m.name FROM films as f LEFT JOIN mpa as m on f.mpa = m.id";

        List<Film> films = jdbcTemplate.query(query, new FilmRowMapper());

        return films;
    }

    @Override
    public Film getFilmById(Long id) {
        String query = "SELECT f.*, m.name FROM films as f LEFT JOIN mpa as m on f.mpa = m.id WHERE f.film_id = ?";

        Film film = jdbcTemplate.query(query, new FilmRowMapper(), id).stream().findAny()
                .orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден"));

        return film;
    }

    @Override
    public void like(Long id, Long userId) {
        String query = "INSERT INTO likes (user_id, film_id) values (?, ?)";

        jdbcTemplate.update(query, userId, id);

    }

    @Override
    public void deleteLike(Long id, Long userId) {
        String query = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";

        jdbcTemplate.update(query, userId, id);
    }

    @Override
    public Set<Likes> getLikes(Long filmId) {
        String query = "SELECT * FROM likes WHERE film_id = ?";

        Set<Likes> likes = new HashSet<>(jdbcTemplate.query(query, new LikesRowMapper(), filmId));

        return likes;
    }
}
