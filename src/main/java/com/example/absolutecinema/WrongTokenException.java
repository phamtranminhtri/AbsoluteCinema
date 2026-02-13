package com.example.absolutecinema;

public class WrongTokenException extends RuntimeException {
    public WrongTokenException(String message) {
        super(message);
    }
}
