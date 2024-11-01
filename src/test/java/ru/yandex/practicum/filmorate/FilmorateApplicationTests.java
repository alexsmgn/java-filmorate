package ru.yandex.practicum.filmorate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRepository.class, FilmRepository.class})
class FilmorateApplicationTests {

    private final UserRepository userStorage;
    private final FilmRepository filmStorage;

    @Test
    public void testFindUnknownUser() {
        try {
            Optional<User> optionalUser = Optional.of(userStorage.getUserById(1L));
        } catch (NotFoundException e) {
            assertEquals("Пользователь с id1не найден", e.getMessage());
        }
    }

    @Test
    public void testFindUnknownFilm() {
        try {
            Optional<Film> optionalFilm = Optional.of(filmStorage.getFilmById(1L));
        } catch (NotFoundException e) {
            assertEquals("Фильм с id 1 не найден", e.getMessage());
        }
    }

    @Test
    public void testFindFilmById() {
        Film filmOne = new Film(1L, "name", "description", LocalDate.of(2000, 01,
                01), 1000, new Mpa(1));

        filmStorage.addFilm(filmOne);

        Optional<Film> optionalFilm = Optional.of(filmStorage.getFilmById(1L));

        assertThat(optionalFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindUserById() {
        User userOne = new User(1L, "mail@mail.ru", "login", "name", LocalDate.of(2000, 01,
                01));

        userStorage.addUser(userOne);

        Optional<User> optionalUser = Optional.of(userStorage.getUserById(1L));

        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testGetUsers() {
        List<User> users = userStorage.getUsers();

        assertEquals(0, users.size());
    }

    @Test
    public void testGetFilms() {
        List<Film> films = filmStorage.getFilms();

        assertEquals(0, films.size());
    }

    @Test
    public void testCreateUserWithoutLogin() {
        User user = new User(1L, "mail", "j j", "name", LocalDate.of(3000, 01, 01));

        userStorage.addUser(user);
    }
}