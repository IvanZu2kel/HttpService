package com.example.httpservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PersonExistException extends Exception{
    public PersonExistException(String message) {
        super(message);
    }
}
