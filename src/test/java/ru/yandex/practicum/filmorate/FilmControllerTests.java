package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmControllerTests {

    @Autowired
    private Validator validator;

    private final Film film = new Film();
    private final String correctDescription = String.valueOf(200);
    private final String incorrectDescription = String.valueOf(201);
    private final LocalDate correctDate = LocalDate.of(1985, 12, 28);

    @BeforeEach
    void setFilm() {
        film.setName("film");
        film.setDescription(correctDescription);
        film.setReleaseDate(LocalDate.of(1985, 12, 28));
        film.setDuration(60);
    }

    @Test
    void correctName() {
        Set<ConstraintViolation<Film>> violation = validator.validate(film);
        assertTrue(violation.isEmpty());

        film.setName(null);
        violation = validator.validate(film);
        assertTrue(violation.isEmpty());
        assertEquals(0, violation.size());

        film.setName("");
        violation = validator.validate(film);
        assertTrue(violation.isEmpty());
        assertEquals(0, violation.size());
    }

    @Test
    void correctDescription() {
        Set<ConstraintViolation<Film>> violation = validator.validate(film);
        assertTrue(violation.isEmpty());

        film.setDescription(incorrectDescription);
        violation = validator.validate(film);
        assertTrue(violation.isEmpty());
        assertEquals(0, violation.size());
    }

    @Test
    void correctDate() {
        Set<ConstraintViolation<Film>> violation = validator.validate(film);
        assertTrue(violation.isEmpty());

        film.setReleaseDate(correctDate.minusDays(1));
        violation = validator.validate(film);
        assertTrue(violation.isEmpty());
        assertEquals(0, violation.size());
    }

    @Test
    void correctDuration() {
        Set<ConstraintViolation<Film>> violation = validator.validate(film);
        assertTrue(violation.isEmpty());

        film.setDuration(-1);
        violation = validator.validate(film);
        assertTrue(violation.isEmpty());
        assertEquals(0, violation.size());
    }
}
