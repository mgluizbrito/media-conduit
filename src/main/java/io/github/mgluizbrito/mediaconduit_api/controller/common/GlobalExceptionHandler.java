package io.github.mgluizbrito.mediaconduit_api.controller.common;

import io.github.mgluizbrito.mediaconduit_api.controller.dto.ExceptionResponse;
import io.github.mgluizbrito.mediaconduit_api.controller.dto.ExceptionsField;
import io.github.mgluizbrito.mediaconduit_api.exceptions.InvalidFieldException;
import io.github.mgluizbrito.mediaconduit_api.exceptions.MediaExtractionException;
import io.github.mgluizbrito.mediaconduit_api.exceptions.RecordDuplicateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ExceptionResponse handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<ExceptionsField> ErrosList = fieldErrors
                .stream()
                .map(fe -> new ExceptionsField(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ExceptionResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation Error",
                ErrosList);
    }

    @ExceptionHandler(RecordDuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse hendlerDuplicateRecordException(RecordDuplicateException e) {
        return ExceptionResponse.conflict(e.getMessage());
    }

    @ExceptionHandler(InvalidFieldException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ExceptionResponse handlerInvalidFieldException(InvalidFieldException e) {
        return new ExceptionResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(), "Invalid Field", List.of(new ExceptionsField(e.getField(), e.getMessage()))
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handlerDeniedAccessException(AccessDeniedException e) {
        return new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(), "ACCESS DENIED", List.of());
    }

    @ExceptionHandler(MediaExtractionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handlerMediaExtractionException(MediaExtractionException e) {
        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of()
        );
    }
}
