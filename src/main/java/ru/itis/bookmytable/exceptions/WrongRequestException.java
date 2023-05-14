package ru.itis.bookmytable.exceptions;

public class WrongRequestException extends RuntimeException {

    public WrongRequestException(String message) {
        super(message);
    }
}
