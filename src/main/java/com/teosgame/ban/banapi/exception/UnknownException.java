package com.teosgame.ban.banapi.exception;

import org.springframework.http.HttpStatus;

public class UnknownException extends BaseException {

    public UnknownException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
