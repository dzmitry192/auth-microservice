package com.innowise.authmicroservice.exception.handler;

import com.innowise.authmicroservice.exception.BadRequestException;
import com.innowise.authmicroservice.exception.ClientAlreadyExistsException;
import com.innowise.authmicroservice.exception.InvalidTokenException;
import com.innowise.authmicroservice.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(ClientAlreadyExistsException.class)
    public ResponseEntity<String> handleException(ClientAlreadyExistsException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleException(InvalidTokenException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleException(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleException(BadRequestException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
