package com.teosgame.ban.banapi.exception;

import org.springframework.http.HttpStatus;

public class InvalidJwtException extends BaseException {
    
    public InvalidJwtException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
