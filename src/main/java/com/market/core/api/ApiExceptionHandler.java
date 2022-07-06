package com.market.core.api;

import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String METHOD_ARG_NOT_VALID_STRING_PATTERN = "%s:%s";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var body = new ErrorResponse();
        body.setErrors(getErrors(ex));
        body.setStatus(status.value());
        body.setPath(((ServletWebRequest) request).getRequest().getServletPath());

        return this.handleExceptionInternal(ex, body, headers, status, request);
    }

    private Collection<String> getErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors().stream()
                .map(error -> String.format(METHOD_ARG_NOT_VALID_STRING_PATTERN, ((FieldError) error).getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    @Data
    public static class ErrorResponse {
        private Timestamp timestamp = Timestamp.from(Instant.now());
        private int status;
        private String path;
        private Collection<String> errors;
    }
}
