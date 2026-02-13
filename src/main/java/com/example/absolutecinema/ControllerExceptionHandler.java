package com.example.absolutecinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(WrongTokenException.class)
    public ResponseEntity<CustomErrorMessage> handleWrongToken() {
        CustomErrorMessage body = new CustomErrorMessage("Wrong token!");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }


}
