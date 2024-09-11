package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTests {

    @Autowired
    private Validator validator;

    private static final User user = new User();

    @BeforeAll
    static void setUser() {
        user.setName("name");
        user.setEmail("user@mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.now().minusDays(1));
    }

    @Test
    void correctLogin() {
        Set<ConstraintViolation<User>> violation = validator.validate(user);
        assertTrue(violation.isEmpty());

        user.setLogin(null);
        violation = validator.validate(user);
        assertTrue(violation.isEmpty());
        assertEquals(0, violation.size());

        user.setLogin("");
        violation = validator.validate(user);
        assertTrue(violation.isEmpty());
        assertEquals(0, violation.size());

        user.setLogin("user login");
        violation = validator.validate(user);
        assertTrue(violation.isEmpty());
        assertEquals(0, violation.size());
    }

    @Test
    void correctEmail() {
        Set<ConstraintViolation<User>> violation = validator.validate(user);
        assertTrue(violation.isEmpty());

        user.setEmail(null);
        violation = validator.validate(user);
        assertTrue(violation.isEmpty());
        assertEquals(0, violation.size());

        user.setEmail("");
        violation = validator.validate(user);
        assertTrue(violation.isEmpty());
        assertEquals(0, violation.size());

        user.setEmail("useremail.ru");
        violation = validator.validate(user);
        assertTrue(violation.isEmpty());
        assertEquals(0, violation.size());
    }

    @Test
    void correctBirthday() {
        Set<ConstraintViolation<User>> violation = validator.validate(user);
        assertTrue(violation.isEmpty());

        user.setBirthday(LocalDate.now().plusDays(1));
        violation = validator.validate(user);
        assertTrue(violation.isEmpty());
        assertEquals(0, violation.size());
    }


}
