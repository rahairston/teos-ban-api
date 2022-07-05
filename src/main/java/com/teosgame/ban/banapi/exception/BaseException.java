package com.teosgame.ban.banapi.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BaseException extends Exception {
    String message;
    String responseBody;
    HttpStatus code;

    public BaseException(String message, HttpStatus code) {
        this.message = message;
        this.responseBody = null;
        this.code = code;
    }

    public BaseException(String message, String responseBody, HttpStatus code) {
        this.message = message;
        this.responseBody = responseBody;
        this.code = code;
    }
}
