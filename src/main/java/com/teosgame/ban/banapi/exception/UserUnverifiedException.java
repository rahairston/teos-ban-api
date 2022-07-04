package com.teosgame.ban.banapi.exception;

import org.springframework.http.HttpStatus;

public class UserUnverifiedException extends BaseException {
    
    public UserUnverifiedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
