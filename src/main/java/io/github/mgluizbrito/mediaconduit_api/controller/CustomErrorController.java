package io.github.mgluizbrito.mediaconduit_api.controller;

import io.github.mgluizbrito.mediaconduit_api.controller.dto.ExceptionResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    private static ExceptionResponse methodNotAllowedExceptionResponse() {
        return new ExceptionResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), "The method used on this endpoint is not allowed", List.of());
    }

    private static ExceptionResponse notFoundExceptionResponse() {
        return new ExceptionResponse(HttpStatus.NOT_FOUND.value(), "The requested endpoint was not found. Please check the URL.", List.of());
    }

    private static ExceptionResponse defaultExceptionResponse() {
        return new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected internal error occurred.", List.of());
    }

    @RequestMapping
    public ExceptionResponse handleError(HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus httpStatus = HttpStatus.valueOf(status != null ? Integer.parseInt(status.toString()) : HttpStatus.INTERNAL_SERVER_ERROR.value());

        if (httpStatus == HttpStatus.NOT_FOUND) return notFoundExceptionResponse();
        if (httpStatus == HttpStatus.METHOD_NOT_ALLOWED) return methodNotAllowedExceptionResponse();

        return defaultExceptionResponse();
    }
}
