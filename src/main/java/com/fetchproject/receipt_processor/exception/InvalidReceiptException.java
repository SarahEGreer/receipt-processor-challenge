package com.fetchproject.receipt_processor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidReceiptException extends RuntimeException {
    public InvalidReceiptException(String message) {
        super(message);
    }
}
