package com.example.redditClone.exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(SubredditNotFoundException.class)
    public ResponseEntity<TimeStampErrorMessage> resourceNotFoundException(SubredditNotFoundException ex,
                                                                           WebRequest request) {
        TimeStampErrorMessage errors = new TimeStampErrorMessage();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());
        errors.setDetails(request.getDescription(false));
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<TimeStampErrorMessage> usernameNotFoundException(UsernameNotFoundException ex,
                                                                           WebRequest request) {
        TimeStampErrorMessage errors = new TimeStampErrorMessage();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());
        errors.setDetails(request.getDescription(false));
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ActivationException.class)
    public ResponseEntity<TimeStampErrorMessage> activationException(ActivationException ex,
                                                                     WebRequest request) {
        TimeStampErrorMessage errors = new TimeStampErrorMessage();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(HttpStatus.BAD_REQUEST.value());
        errors.setDetails(request.getDescription(false));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // @Validate For Validating Path Variables and Request Parameters
    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }


    // error handle for @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());

        //Get all fields errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> String.format(x.getField() + ' ' + "field" + ' ' + x.getDefaultMessage()))
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);

    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<TimeStampErrorMessage> badCredentialsHandler(Exception ex, WebRequest request) {
        TimeStampErrorMessage errors = new TimeStampErrorMessage();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(HttpStatus.BAD_REQUEST.value());
        errors.setDetails(request.getDescription(false));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequestRejectedException.class)
    public ResponseEntity<TimeStampErrorMessage> serverExcpetionHandler(Exception ex, WebRequest request) {
        TimeStampErrorMessage errors = new TimeStampErrorMessage();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errors.setDetails(request.getDescription(false));
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
