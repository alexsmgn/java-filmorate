package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Likes;

import java.util.List;
import java.util.Set;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film newFilm);

    List<Film> getFilms();

    Film getFilmById(Long id);

    void like(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    Set<Likes> getLikes(Long filmId);
}
