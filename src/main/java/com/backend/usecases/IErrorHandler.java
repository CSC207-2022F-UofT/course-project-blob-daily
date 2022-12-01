package com.backend.usecases;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface IErrorHandler {
    default ResponseEntity<Object> logError(Exception exception, HttpStatus status) {
        return null;
    }
}
