package com.coderone95.test.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchQuestionException extends RuntimeException{
    public NoSuchQuestionException() {
        super();
    }

    public NoSuchQuestionException(String message) {
        super(message);
    }
}
