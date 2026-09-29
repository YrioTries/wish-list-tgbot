package ru.javabot.user.exception_handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.javabot.interaction_api.exeption.ApiError;
import ru.javabot.interaction_api.exeption.BadRequestException;
import ru.javabot.interaction_api.exeption.ConflictException;
import ru.javabot.interaction_api.exeption.NotFoundException;

@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(final BadRequestException e) {
        log.info("Получен статус 400 BAD_REQUEST {}", e.getMessage(), e);
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .message(e.getMessage())
                .reason("Требуемый ресурс является некорректным.")
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.info("Получен статус 404 NOT_FOUND {}", e.getMessage(), e);
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .message(e.getMessage())
                .reason("Требуемый ресурс не был найден.")
                .build();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerValidationException(Exception e) {
        log.debug("Получен статус 409 CONFLICT {}", e.getMessage());
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.toString())
                .message(e.getMessage())
                .reason("Запрос - это источник ошибки CONFLICT")
                .build();
    }
}
