package com.amit.converse.chat.exceptions;

public class ConverseException extends RuntimeException {
    public ConverseException(String errorMessage) {
        super(errorMessage);
    }
}