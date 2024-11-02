package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ErrorResponse handleValidationException(final ValidationException e) {
        return new ErrorResponse("Ошибка валидации", e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponse handleNotFountException(final NotFoundException e) {
        return new ErrorResponse("Ошибка", e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponse handleConditionNotMetException(final ConditionsNotMetException e) {
        return new ErrorResponse("Ошибка", e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponse handleDuplicateDataException(final DuplicatedDataException e) {
        return new ErrorResponse("Ошибка", e.getMessage());
    }

}
