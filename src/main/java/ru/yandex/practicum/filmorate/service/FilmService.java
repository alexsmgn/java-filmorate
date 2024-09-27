package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.controller.ErrorResponse;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;

@Slf4j
@Service
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Collection<Film> findAll() {
        return inMemoryFilmStorage.films.values();
    }

    public Film getFilmById(long filmId) {
        if (inMemoryFilmStorage.films.containsKey(filmId)) {
            return inMemoryFilmStorage.films.get(filmId);
        } else {
            throw new NotFoundException("фильм не найден");
        }
    }

    public void addLike(long filmId, long userId) {
        if (!inMemoryUserStorage.users.containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        Film film = getFilmById(filmId);
        film.getLikes().add(userId);
        log.info("К фильму с id {} добавлен лайк от пользователя с id {}", filmId, userId);
    }

    public void delLike(long filmId, long userId) {
        if (!inMemoryUserStorage.users.containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        Film film = inMemoryFilmStorage.films.get(filmId);
        film.getLikes().remove(userId);
        log.info("У фильма с id {} удален лайк пользователя с id {}", filmId, userId);
    }

    public Collection<Film> getPopularFilms(int count) {
        return findAll().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(10)
                .toList();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFountException(final NotFoundException e) {
        return new ErrorResponse("Ошибка", e.getMessage());
    }
}
