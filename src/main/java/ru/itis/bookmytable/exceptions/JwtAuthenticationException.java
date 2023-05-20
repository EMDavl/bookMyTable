package ru.itis.bookmytable.exceptions;


public class JwtAuthenticationException extends RuntimeException{

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
