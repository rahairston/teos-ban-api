package com.teosgame.ban.banapi.exception;
import org.springframework.http.HttpStatus;

public class TwitchResponseException extends BaseException {
    
    public TwitchResponseException(String message, HttpStatus code) {
        super(message, code);
    }
}
