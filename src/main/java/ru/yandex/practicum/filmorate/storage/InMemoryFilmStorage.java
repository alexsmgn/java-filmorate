package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    public final Map<Long, Film> films = new HashMap<>();
    private final LocalDate dayOfCreationCinema = LocalDate.of(1895, 12, 28);

    private Long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(getNextId());
        filmValidator(film);
        films.put(film.getId(), film);
        log.info("Фильм {} успешно добавлен", film);
        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("Не указан Id фильма");
            throw new ValidationException("id фильма должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            filmValidator(newFilm);

            oldFilm.setName(newFilm.getName());
            log.info("Название фильма {} изменено", oldFilm);
            oldFilm.setDescription(newFilm.getDescription());
            log.info("Описание фильма {} изменено", oldFilm);
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            log.info("Дата выходи фильма {} изменена", oldFilm);
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Длительность фильма {} изменена", oldFilm);
            return oldFilm;
        }
        log.error("Фильм с id = {} не найден", newFilm.getId());
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    @Override
    public void deleteFilm(Long id) {
        films.remove(id);
        log.info("Филь с id {} удален", id);
    }

    @Override
    public void filmValidator(Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Пустое имя фильма при добавлении");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Слишком длинное описание фильма при добавлении");
            throw new ValidationException("Слишком длинное описание");
        }
        if (film.getReleaseDate().isBefore(dayOfCreationCinema)) {
            log.error("неверная дата выходи фильма при добавлении");
            throw new ValidationException("Указана неверная дата");
        }
        if (film.getDuration() < 0) {
            log.error("При добавлении фильма длительность указана меньше 0");
            throw new ValidationException("Длительность фильма не может быть меньше 0");
        }
    }
}

