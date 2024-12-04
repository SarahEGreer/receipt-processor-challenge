package com.fetchproject.receipt_processor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ReceiptNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleReceiptNotFoundException(ReceiptNotFoundException ex, WebRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidReceiptException.class)
    public ResponseEntity<Map<String, String>> handleInvalidReceiptException(InvalidReceiptException ex, WebRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "The receipt is invalid.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
