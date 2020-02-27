package com.transport.app.rest.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TransportAppExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                getErrorDetail(e, e.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(
                        FieldError::getField, FieldError::getDefaultMessage))));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                e.getMessage(),
                getErrorDetail(e, Collections.singletonMap(e.getMethod(), e.getMessage())));
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<CustomErrorResponse> handleExceptionInternal(ConstraintViolationException e, WebRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                getErrorDetail(e, Collections.singletonMap(e.getCause().toString(), e.getMessage())));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<CustomErrorResponse> handleExceptionInternal(DataIntegrityViolationException e, WebRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                getErrorDetail(e, Collections.singletonMap(e.getCause().toString(), e.getMessage())));
//        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleNotFoundException(NotFoundException e, WebRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                getErrorDetail(e, Collections.singletonMap("id", e.getId()))
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    private Map<String, Object> getErrorDetail(Exception e, Map<String, Object> errors) {
        Map<String, Object> errorDetail = new HashMap<>();
        errorDetail.put("exception", e.getClass().getSimpleName());
        errorDetail.put("errors", errors);
        return errorDetail;
    }
}
