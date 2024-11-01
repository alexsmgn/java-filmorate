package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenreStorage {

    Genre getGenreById(Integer id);

    Set<Genre> getGenres();

    Set<Genre> getFilmGenres(Long id);

    void setFilmGenres(Film film);

    void clearFilmGenre(Long id);
}
