package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Likes;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, GenreStorage genreStorage, JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Film getFilmById(Long id) {
        Film film = filmStorage.getFilmById(id);

        load(film);

        return film;
    }

    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();

        loadButch(films);

        return films;
    }

    public Film update(Film film) {
        getFilmById(film.getId());
        genreStorage.clearFilmGenre(film.getId());
        genreStorage.setFilmGenres(film);
        filmStorage.updateFilm(film);

        return film;
    }

    public Film addFilm(Film film) {
        filmStorage.addFilm(film);
        genreStorage.setFilmGenres(film);
        return film;
    }

    public void like(Long id, Long userId) {
        Film film = filmStorage.getFilmById(id);
        userStorage.getUserById(userId);

        if (film.isLiked(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " уже лайкнул этот фильм");
        } else {
            filmStorage.like(id, userId);
        }
    }

    public void delLike(Long id, Long userId) {
        Film film = getFilmById(id);
        userStorage.getUserById(userId);

        if (film.isLiked(userId)) {
            filmStorage.deleteLike(id, userId);
        } else {
            throw new NotFoundException("Пользователь с id " + userId + " не лайкал этот фильм");
        }
    }

    public List<Film> getPopularFilms(int count) {
        return getFilms().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f1, Film f2) {
        int result = f2.getLikes().size() - f1.getLikes().size();

        return result;
    }

    private void loadButch(List<Film> films) {
        String inQuery = String.join(",", Collections.nCopies(films.size(), "?"));
        final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        final String sqlQuery = "SELECT * FROM genre g, film_genres fg LEFT JOIN likes l ON fg.film_id = l.film_id " +
                "WHERE fg.id = g.id AND fg.film_id IN (" + inQuery + ")";

        jdbcTemplate.query(sqlQuery, (rs) -> {
            final Film film = filmById.get(rs.getLong("film_id"));
            film.addGenre(Genre.makeGenre(rs, 0));
            film.addLike(Likes.addLike(rs, 0));
        }, films.stream().map(Film::getId).toArray());
    }

    private void load(Film film) {
        String query = "SELECT * FROM genre g, film_genres fg LEFT JOIN likes l ON fg.film_id = l.film_id " +
                "WHERE fg.id = g.id AND fg.film_id = ?";

        jdbcTemplate.query(query, rs -> {
            film.addGenre(Genre.makeGenre(rs, 0));
            film.addLike(Likes.addLike(rs, 0));
        }, film.getId());
    }
}
