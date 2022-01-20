package com.example.httpservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidInputFormat extends Exception{
    public InvalidInputFormat(String message) {
        super(message);
    }
}
