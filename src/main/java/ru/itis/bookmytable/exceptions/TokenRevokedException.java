package ru.itis.bookmytable.exceptions;

public class TokenRevokedException extends RuntimeException{

    public TokenRevokedException(String message) {
        super(message);
    }
}
