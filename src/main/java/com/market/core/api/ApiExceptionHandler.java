package com.market.core.api;

import com.market.core.domain.EntityNotFoundException;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String METHOD_ARG_NOT_VALID_STRING_PATTERN = "%s:%s";
    public static final String METHOD_GENERAL_MSG_PATTERN = "msg:%s";

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        var body = getBody(request, List.of(String.format(METHOD_GENERAL_MSG_PATTERN, ex.getMessage())), HttpStatus.NOT_FOUND.value());

        return this.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public final ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        var body = getBody(request, List.of(String.format(METHOD_GENERAL_MSG_PATTERN, ex.getMessage())), HttpStatus.BAD_REQUEST.value());

        return this.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var body = getBody(request, getErrors(ex), status.value());

        return this.handleExceptionInternal(ex, body, headers, status, request);
    }

    private ErrorResponse getBody(WebRequest request, Collection<String> errors, int status) {
        var body = new ErrorResponse();
        body.setErrors(errors);
        body.setStatus(status);
        body.setPath(((ServletWebRequest) request).getRequest().getServletPath());
        return body;
    }

    private Collection<String> getErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors().stream().map(error -> String.format(METHOD_ARG_NOT_VALID_STRING_PATTERN, ((FieldError) error).getField(), error.getDefaultMessage())).collect(Collectors.toList());
    }

    @Data
    public static class ErrorResponse {
        private Timestamp timestamp = Timestamp.from(Instant.now());
        private int status;
        private String path;
        private Date date = new Date();
        private Collection<String> errors = new ArrayList<>();
    }
}
